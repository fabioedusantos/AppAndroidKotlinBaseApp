package br.com.fbsantos.baseapp.util.helpers

import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

/**
 * Helper para exibição centralizada de mensagens via Toast.
 *
 * Utiliza [StateFlow] para disparar toasts a partir de qualquer ponto
 * da aplicação e um listener @Composable para exibir a mensagem
 * no contexto atual da UI.
 *
 * Vantagens:
 * - Centraliza a exibição de toasts.
 * - Integração simples com Compose via um listener.
 * - Evita código repetido de `Toast.makeText` espalhado pelo app.
 */
object ToastManager {
    private val _toastMessage = MutableStateFlow<String?>(null)
    /** Fluxo somente-leitura da mensagem de Toast. */
    val toastMessage = _toastMessage.asStateFlow()

    /**
     * Define uma nova mensagem a ser exibida via Toast.
     *
     * @param message Texto da mensagem.
     */
    fun show(message: String) {
        _toastMessage.value = message
    }

    /**
     * Limpa a mensagem atual, prevenindo exibições repetidas.
     */
    fun clear() {
        _toastMessage.value = null
    }

    /**
     * Observa mudanças na mensagem e exibe um Toast quando necessário.
     *
     * Deve ser chamado dentro da hierarquia Compose (por exemplo, na tela root),
     * garantindo que o contexto esteja disponível.
     *
     * Comportamento:
     * - Exibe o Toast com duração longa.
     * - Após exibir, limpa a mensagem chamando [clear].
     */
    @Composable
    fun listener() {
        val context = LocalContext.current
        val toastError by toastMessage.collectAsState()
        LaunchedEffect(toastError) {
            toastError?.let {
                Toast.makeText(context, it, Toast.LENGTH_LONG).show()
                clear()
            }
        }
    }
}