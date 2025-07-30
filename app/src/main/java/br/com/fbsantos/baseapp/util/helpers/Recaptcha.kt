package br.com.fbsantos.baseapp.util.helpers

import android.app.Application
import android.util.Log
import br.com.fbsantos.baseapp.config.AppConfig
import com.google.android.recaptcha.Recaptcha
import com.google.android.recaptcha.RecaptchaAction
import com.google.android.recaptcha.RecaptchaClient
import com.google.android.recaptcha.RecaptchaException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

/**
 * Helper para gerenciamento da API **Google reCAPTCHA** no Android.
 *
 * Centraliza:
 * - Inicialização do cliente reCAPTCHA.
 * - Execução de ações de verificação (por exemplo, login).
 * - Tratamento de callbacks de sucesso/erro.
 *
 * ⚠ É necessário configurar a **SITE_KEY** em [AppConfig.RECAPTCHA_SITE_KEY].
 */
object Recaptcha {
    private const val TAG = "reCAPTCHA"
    private const val SITE_KEY = AppConfig.RECAPTCHA_SITE_KEY

    private var recaptchaClient: RecaptchaClient? = null
    private val recaptchaScope = CoroutineScope(Dispatchers.IO)

    /**
     * Inicializa o cliente reCAPTCHA de forma assíncrona.
     *
     * @param application Instância de [Application], usada para inicializar o cliente.
     */
    fun initialize(application: Application) {
        recaptchaScope.launch {
            try {
                recaptchaClient = Recaptcha.fetchClient(application, SITE_KEY)
                if (AppConfig.IS_DEBUG) Log.d(TAG, "Cliente inicializado")
            } catch (e: RecaptchaException) {
                if (AppConfig.IS_DEBUG) Log.e(TAG, "Erro ao inicializar: ${e.message}", e)
            }
        }
    }

    /**
     * Executa a ação de login definida no reCAPTCHA.
     *
     * Este método privado chama a API passando a ação [RecaptchaAction.LOGIN] e
     * retorna o token obtido para posterior validação no backend.
     *
     * @param onTokenReceived Callback com `(token, siteKey)` em caso de sucesso
     *                        ou `(null, null)` em falha.
     */
    private fun executeLoginAction(onTokenReceived: (String?, String?) -> Unit) {
        val client = recaptchaClient ?: return onTokenReceived(null, null)
        recaptchaScope.launch {
            try {
                client.execute(RecaptchaAction.LOGIN)
                    .onSuccess { token ->
                        onTokenReceived(token, SITE_KEY)
                    }
                    .onFailure { exception ->
                        if (AppConfig.IS_DEBUG) Log.e(TAG, "Erro na execução: ${exception.message}", exception)
                        onTokenReceived(null, null)
                    }
            } catch (e: Exception) {
                if (AppConfig.IS_DEBUG) Log.e(TAG, "Exceção inesperada: ${e.message}", e)
                onTokenReceived(null, null)
            }
        }
    }

    /**
     * Executa a verificação reCAPTCHA com callbacks para pré e pós-execução.
     *
     * Fluxo:
     * 1. Executa [before], se definido.
     * 2. Chama [executeLoginAction] para obter o token.
     * 3. Executa [after], se definido, logo após a resposta.
     * 4. Em caso de sucesso, chama [onSuccess] com `(token, siteKey)`.
     * 5. Em caso de erro, chama [onError] com mensagem amigável.
     *
     * @param before Ação opcional a ser executada antes da chamada (ex.: exibir loading).
     * @param after Ação opcional a ser executada após a chamada (ex.: esconder loading).
     * @param onSuccess Callback chamado em sucesso, contendo `(token, siteKey)`.
     * @param onError Callback chamado em falha, com mensagem de erro.
     */
    fun exec(
        before: (() -> Unit)? = null,
        after: (() -> Unit)? = null,
        onSuccess: (String, String) -> Unit,
        onError: (String) -> Unit
    ) {
        before?.invoke()
        executeLoginAction { token, siteKey ->
            after?.invoke()
            if (token != null && siteKey != null) {
                onSuccess(token, siteKey)
            } else {
                onError("Falha ao validar reCAPTCHA. Tente novamente.")
            }
        }
    }
}