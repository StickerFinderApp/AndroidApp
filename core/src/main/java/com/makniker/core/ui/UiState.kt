package com.makniker.core.ui

sealed class UiState<T> {
    class Loading<T> : UiState<T>()
    class Success<T>(val data: T) : UiState<T>()
    class Failure<T>(val e: Throwable) : UiState<T>()
}