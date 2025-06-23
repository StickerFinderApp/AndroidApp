package com.makniker.camera_presentation

import android.graphics.Bitmap
import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.makniker.camera_domain.CameraRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CameraViewModel @Inject constructor(private val repository: CameraRepository): ViewModel() {

    private val _saveResult = MutableLiveData<Result<Uri>>()
    val saveResult: LiveData<Result<Uri>> = _saveResult

    fun saveSticker(bitmap: Bitmap) {
        viewModelScope.launch(Dispatchers.IO) {
            _saveResult.postValue(repository.saveStickerToDisc(bitmap))
        }
    }
}