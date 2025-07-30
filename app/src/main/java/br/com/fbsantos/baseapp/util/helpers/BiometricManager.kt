package br.com.fbsantos.baseapp.util.helpers

import android.content.Context
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricPrompt
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity

/**
 * Helper para autenticação biométrica (ou credencial do dispositivo) usando a API BiometricPrompt.
 *
 * Centraliza:
 * - Verificação de disponibilidade de autenticação biométrica/credencial.
 * - Exibição do prompt de autenticação com callbacks de sucesso e erro.
 *
 * Observação: como o nome deste objeto colide com `androidx.biometric.BiometricManager`,
 * as referências à classe da AndroidX são totalmente qualificadas dentro deste helper.
 */
object BiometricManager {
    /**
     * Verifica se há suporte para autenticação biométrica (ou credencial do dispositivo) no contexto.
     *
     * Utiliza os autenticadores:
     * - BIOMETRIC_STRONG (biometria forte, ex.: impressão digital/rosto com forte segurança)
     * - DEVICE_CREDENTIAL (PIN/padrão/senha do dispositivo)
     *
     * @param context Contexto utilizado para consultar a disponibilidade.
     * @return `true` se a autenticação é suportada e possível no momento, `false` caso contrário.
     */
    fun isAvailable(context: Context): Boolean {
        val biometricManager = BiometricManager.from(context)
        val authFlags = BiometricManager.Authenticators.BIOMETRIC_STRONG or
                BiometricManager.Authenticators.DEVICE_CREDENTIAL

        return biometricManager.canAuthenticate(authFlags) ==
                BiometricManager.BIOMETRIC_SUCCESS
    }

    /**
     * Exibe o prompt de autenticação biométrica (ou credencial do dispositivo).
     *
     * Requisitos:
     * - O `context` deve ser uma `FragmentActivity` válida.
     * - O dispositivo deve oferecer suporte aos autenticadores configurados.
     *
     * Callbacks:
     * - [onSuccess]: chamado quando a autenticação for concluída com sucesso.
     * - [onError]: chamado para erros, cancelamento, falhas ou `context` inválido.
     *
     * @param context Contexto (deve ser `FragmentActivity`) onde o prompt será exibido.
     * @param title Título exibido no prompt.
     * @param subtitle Subtítulo exibido no prompt.
     * @param onSuccess Callback invocado em autenticação bem-sucedida.
     * @param onError Callback invocado em erro ou falha; recebe a mensagem de erro.
     */
    fun showPrompt(
        context: Context,
        title: String = "Autenticação",
        subtitle: String = "Desbloqueie o dispositivo para continuar",
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        val executor = ContextCompat.getMainExecutor(context)
        val activity = context as? FragmentActivity ?: run {
            onError("Context inválido para autenticação biométrica")
            return
        }

        val promptInfo = BiometricPrompt.PromptInfo.Builder()
            .setTitle(title)
            .setSubtitle(subtitle)
            .setAllowedAuthenticators(
                BiometricManager.Authenticators.BIOMETRIC_STRONG or
                        BiometricManager.Authenticators.DEVICE_CREDENTIAL
            )
            .build()

        val biometricPrompt = BiometricPrompt(
            activity,
            executor,
            object : BiometricPrompt.AuthenticationCallback() {
                override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                    onSuccess()
                }

                override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
                    onError(errString.toString())
                }

                override fun onAuthenticationFailed() {
                    onError("Autenticação falhou. Tente novamente.")
                }
            })

        biometricPrompt.authenticate(promptInfo)
    }
}