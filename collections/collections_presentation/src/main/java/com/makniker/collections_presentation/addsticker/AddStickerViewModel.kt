package com.makniker.collections_presentation.addsticker

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.makniker.collections_domain.CollectionRepository
import com.makniker.collections_domain.StickerDTO
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddStickerViewModel @Inject constructor(private val repository: CollectionRepository) :
    ViewModel() {
    var name by mutableStateOf("")
        private set

    var author by mutableStateOf("")
        private set

    var uri by mutableStateOf("")
        private set

    fun updateName(newName: String) {
        name = newName
    }

    fun updateAuthor(newAuthor: String) {
        author = newAuthor
    }

    fun updateUri(uri: String) {
        this.uri = uri
    }

    fun saveSticker(onEnd: () -> Unit) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.addSticker(
                StickerDTO(
                    id = null, uri = uri, author = author, name = name
                )
            )
        }
        onEnd()
    }

    fun isFormValid(): Boolean = name.isNotBlank() && author.isNotBlank()
}