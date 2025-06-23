package com.makniker.collections_presentation.addsticker

import android.net.Uri
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.makniker.core.ui.Routes
import com.makniker.core.ui.components.StickerImage

@Composable
fun AddStickerScreen(
    modifier: Modifier = Modifier, navController: NavHostController, addStickerViewModel: AddStickerViewModel, stickerUri: Uri
) {
    Box(modifier = modifier.fillMaxSize()) {
        addStickerViewModel.updateUri(stickerUri.toString())
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            StickerImage(modifier.fillMaxHeight(0.5f), stickerUri)

            OutlinedTextField(
                value = addStickerViewModel.name,
                onValueChange = addStickerViewModel::updateName,
                label = { Text("Название стикера") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next)
            )

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = addStickerViewModel.author,
                onValueChange = addStickerViewModel::updateAuthor,
                label = { Text("Автор") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done)
            )

            Spacer(modifier = Modifier.weight(1f))

            Button(
                onClick =  {
                    addStickerViewModel.saveSticker( {
                        navController.navigate(Routes.COLLECTION)
                    } ) },
                enabled = addStickerViewModel.isFormValid()
            ) {
                Text("Сохранить")
            }
        }
    }
}