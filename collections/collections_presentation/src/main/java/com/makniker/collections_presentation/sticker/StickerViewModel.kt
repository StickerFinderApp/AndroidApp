package com.makniker.collections_presentation.sticker

import androidx.core.net.toUri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.makniker.collections_domain.CollectionRepository
import com.makniker.core.ui.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class StickerViewModel @Inject constructor(private val repository: CollectionRepository) :
    ViewModel() {

    private val _uiState = MutableStateFlow<UiState<StickerUiModel>>(UiState.Loading())
    val uiState: StateFlow<UiState<StickerUiModel>> = _uiState

    fun loadSticker(id: Int) {
        _uiState.value = UiState.Loading()
        viewModelScope.launch(Dispatchers.IO) {
            _uiState.value =
                repository.getSticker(id)
                .fold(
                    onSuccess = {
                        UiState.Success(
                            StickerUiModel(
                                id = it.id ?: 0,
                                uri = it.uri.toUri(),
                                name = it.name,
                                author = it.author
                            )
                        ) 
                    },
                    onFailure = { UiState.Failure(it) }
                )
        }
    }
} 