package com.makniker.collections_presentation.collection

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
class CollectionViewModel @Inject constructor(private val repository: CollectionRepository) :
    ViewModel() {

    private val _uiState =
        MutableStateFlow<UiState<List<StickerCollectionUiModel>>>(UiState.Loading())
    val uiState: StateFlow<UiState<List<StickerCollectionUiModel>>> = _uiState

    fun loadCollection() {
        _uiState.value = UiState.Loading()
        viewModelScope.launch(Dispatchers.IO) {
            _uiState.value = repository.fetchSavedCollection()
                .fold(
                    onSuccess = { UiState.Success(it.map { StickerCollectionUiModel(
                        id = it.id ?: 0,
                        uri = it.uri.toUri()
                    ) }) },
                    onFailure = { UiState.Failure(it) })
        }
    }

}