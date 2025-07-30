package br.com.fbsantos.baseapp.util.helpers

import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

/**
 * Helper para gerenciamento centralizado de mensagens exibidas em Snackbars.
 *
 * Utiliza [StateFlow] para permitir que a UI reaja automaticamente
 * a mudanças de estado na mensagem, exibindo ou ocultando o Snackbar
 * conforme necessário.
 *
 * Vantagens:
 * - Centralização da lógica de exibição de snackbars.
 * - Integração simples com Jetpack Compose via coleta de fluxo (collectAsState).
 * - Fácil limpeza da mensagem após exibição.
 *
 * Uso típico no Compose:
 * ```
 * val snackbarMessage by SnackbarManager.snackbarMessage.collectAsState()
 * snackbarMessage?.let { msg ->
 *     // Exibir Snackbar usando msg
 *     SnackbarManager.clear()
 * }
 * ```
 */
object SnackbarManager {

    private val _snackbarMessage = MutableStateFlow<String?>(null)
    /** Fluxo somente leitura para observação de mensagens. */
    val snackbarMessage = _snackbarMessage.asStateFlow()

    /**
     * Define uma nova mensagem para ser exibida no Snackbar.
     *
     * @param message Texto da mensagem.
     */
    fun show(message: String) {
        _snackbarMessage.value = message
    }

    /**
     * Limpa a mensagem atual, ocultando o Snackbar na UI.
     */
    fun clear() {
        _snackbarMessage.value = null
    }

    /**
     * Observa automaticamente novas mensagens e exibe o Snackbar.
     *
     * Deve ser chamado dentro de um Composable que tenha acesso
     * a um [SnackbarHostState], como em um Scaffold.
     *
     * Ao exibir a mensagem:
     * - Mostra o Snackbar com botão de dispensa.
     * - Define a duração como [SnackbarDuration.Long].
     * - Limpa a mensagem após a exibição para evitar repetição.
     *
     * @param snackbarHostState Host state do Snackbar, geralmente vindo do Scaffold.
     */
    @Composable
    fun listener(snackbarHostState: SnackbarHostState) {
        val snackbarMessage by snackbarMessage.collectAsState()
        LaunchedEffect(snackbarMessage) {
            snackbarMessage?.let {
                snackbarHostState.showSnackbar(
                    message = it,
                    withDismissAction = true,
                    duration = SnackbarDuration.Long
                )
                clear()
            }
        }
    }
}