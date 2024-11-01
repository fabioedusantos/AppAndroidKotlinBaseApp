package br.com.fbsantos.baseapp.util

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

object SnackbarManager {

    private val _snackbarMessage = MutableStateFlow<String?>(null)
    val snackbarMessage = _snackbarMessage.asStateFlow()

    fun show(message: String) {
        _snackbarMessage.value = message
    }

    fun clear() {
        _snackbarMessage.value = null
    }
}