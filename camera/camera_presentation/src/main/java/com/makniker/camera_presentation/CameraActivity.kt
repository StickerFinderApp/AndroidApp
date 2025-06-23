package com.makniker.camera_presentation

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Matrix
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.camera.core.AspectRatio
import androidx.camera.core.Camera
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.makniker.camera_presentation.Constants.LABELS_PATH
import com.makniker.camera_presentation.Constants.MODEL_PATH
import com.makniker.camera_presentation.databinding.CameraLayoutBinding
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import androidx.core.graphics.createBitmap
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay

@AndroidEntryPoint
class CameraActivity : AppCompatActivity(), Detector.DetectorListener {
    private lateinit var binding: CameraLayoutBinding
    private val isFrontCamera = false

    private var preview: Preview? = null
    private var imageAnalyzer: ImageAnalysis? = null
    private var camera: Camera? = null
    private var cameraProvider: ProcessCameraProvider? = null
    private var detector: Detector? = null

    private lateinit var cameraExecutor: ExecutorService

    private val viewModel: CameraViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = CameraLayoutBinding.inflate(layoutInflater)
        setContentView(binding.root)

        cameraExecutor = Executors.newSingleThreadExecutor()

        cameraExecutor.execute {
            detector = Detector(baseContext, MODEL_PATH, LABELS_PATH, this) {
                toast(it)
            }
        }

        if (allPermissionsGranted()) {
            startCamera()
        } else {
            requestPermissionLauncher.launch(REQUIRED_PERMISSIONS)
            startCamera()
        }

        binding.overlay.onBoxClicked = { box ->
            val bitmap = currentBitmap
            if (bitmap != null) {
                val croppedBitmap = cropBitmap(bitmap, box)
                showSaveDialog(croppedBitmap)
            } else {
                toast("Не удалось получить текущий кадр")
            }
        }

        viewModel.saveResult.observe(this) { result ->
            result.onSuccess { uri ->
                setResult(RESULT_OK, Intent().apply {
                    putExtra("captured_image_uri", uri.toString())
                })
                finish()
            }.onFailure { error ->
                Log.e("MY_TAG", error.message.toString())
                toast("Writing on disc failed")
            }
        }

    }

    private fun cropBitmap(bitmap: Bitmap, box: BoundingBox): Bitmap {
        val left = (box.x1 * bitmap.width).toInt()
        val top = (box.y1 * bitmap.height).toInt()
        val right = (box.x2 * bitmap.width).toInt()
        val bottom = (box.y2 * bitmap.height).toInt()
        return Bitmap.createBitmap(bitmap, left, top, right - left, bottom - top)
    }

    private fun showSaveDialog(croppedBitmap: Bitmap) {
        val dialogView = layoutInflater.inflate(R.layout.dialog_save_image, null)
        val imageView = dialogView.findViewById<ImageView>(R.id.dialog_image)
        val saveButton = dialogView.findViewById<Button>(R.id.dialog_save_button)
        val cancelButton = dialogView.findViewById<Button>(R.id.dialog_cancel_button)

        imageView.setImageBitmap(croppedBitmap)

        val dialog = AlertDialog.Builder(this)
            .setView(dialogView)
            .setCancelable(false)
            .create()

        saveButton.setOnClickListener {
            viewModel.saveSticker(croppedBitmap)
            currentBitmap = null
            dialog.dismiss()
        }

        cancelButton.setOnClickListener {
            dialog.dismiss()
        }

        dialog.show()
    }

    private fun startCamera() {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(this)
        cameraProviderFuture.addListener({
            cameraProvider = cameraProviderFuture.get()
            bindCameraUseCases()
        }, ContextCompat.getMainExecutor(this))
    }

    private var currentBitmap: Bitmap? = null

    private fun bindCameraUseCases() {
        val cameraProvider =
            cameraProvider ?: throw IllegalStateException("Camera initialization failed.")

        val rotation = binding.viewFinder.display.rotation

        val cameraSelector =
            CameraSelector.Builder().requireLensFacing(CameraSelector.LENS_FACING_BACK).build()

        preview = Preview.Builder().setTargetAspectRatio(AspectRatio.RATIO_4_3)
            .setTargetRotation(rotation).build()

        imageAnalyzer = ImageAnalysis.Builder().setTargetAspectRatio(AspectRatio.RATIO_4_3)
            .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
            .setTargetRotation(binding.viewFinder.display.rotation)
            .setOutputImageFormat(ImageAnalysis.OUTPUT_IMAGE_FORMAT_RGBA_8888).build()

        imageAnalyzer?.setAnalyzer(cameraExecutor) { imageProxy ->
            val bitmapBuffer = createBitmap(imageProxy.width, imageProxy.height)
            imageProxy.use { bitmapBuffer.copyPixelsFromBuffer(imageProxy.planes[0].buffer) }
            imageProxy.close()

            val matrix = Matrix().apply {
                postRotate(imageProxy.imageInfo.rotationDegrees.toFloat())

                if (isFrontCamera) {
                    postScale(
                        -1f, 1f, imageProxy.width.toFloat(), imageProxy.height.toFloat()
                    )
                }
            }

            val rotatedBitmap = Bitmap.createBitmap(
                bitmapBuffer, 0, 0, bitmapBuffer.width, bitmapBuffer.height, matrix, true
            )
            currentBitmap = rotatedBitmap

            detector?.detect(rotatedBitmap)

        }

        cameraProvider.unbindAll()

        try {
            camera = cameraProvider.bindToLifecycle(
                this, cameraSelector, preview, imageAnalyzer
            )

            preview?.surfaceProvider = binding.viewFinder.surfaceProvider
        } catch (exc: Exception) {
            Log.e(TAG, "Use case binding failed", exc)
        }
    }

    private fun allPermissionsGranted() = REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(baseContext, it) == PackageManager.PERMISSION_GRANTED
    }

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) {
        if (it[Manifest.permission.CAMERA] == true) {
            startCamera()
        }
    }

    private fun toast(message: String) {
        runOnUiThread {
            Toast.makeText(baseContext, message, Toast.LENGTH_LONG).show()
        }
    }

    override fun onDestroy() {
        Log.d("MY_TAG", "ON DESTROY")
        super.onDestroy()
        try {
            binding.overlay.clear()
            imageAnalyzer?.clearAnalyzer()
            cameraProvider?.unbindAll()
            cameraExecutor.shutdownNow()
            //detector?.close()
            detector = null
        } catch (e: Exception) {
            Log.e("MY_TAG", "Error releasing resources", e)
        }
    }

    override fun onStop() {
        Log.d("MY_TAG", "ON STOP")
        super.onStop()
    }

    override fun onResume() {
        super.onResume()

    }

    companion object {
        private const val TAG = "Camera"
        private const val REQUEST_CODE_PERMISSIONS = 10
        private val REQUIRED_PERMISSIONS = mutableListOf(
            Manifest.permission.CAMERA
        ).toTypedArray()
    }

    override fun onEmptyDetect() {
        runOnUiThread {
            binding.overlay.clear()
        }
    }

    override fun onDetect(boundingBoxes: List<BoundingBox>, inferenceTime: Long) {
        runOnUiThread {
            binding.inferenceTime.text = "${inferenceTime}ms"
            binding.overlay.apply {
                setResults(boundingBoxes)
                invalidate()
            }
        }
    }
}