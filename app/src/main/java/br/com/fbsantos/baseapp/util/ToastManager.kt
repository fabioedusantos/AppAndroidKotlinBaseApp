package br.com.fbsantos.baseapp.util

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

object ToastManager {
    private val _toastMessage = MutableStateFlow<String?>(null)
    val toastMessage = _toastMessage.asStateFlow()

    fun show(message: String) {
        _toastMessage.value = message
    }

    fun clear() {
        _toastMessage.value = null
    }
}