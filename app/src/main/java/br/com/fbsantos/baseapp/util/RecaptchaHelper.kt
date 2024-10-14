package br.com.fbsantos.baseapp.util

import android.app.Application
import android.util.Log
import com.google.android.recaptcha.Recaptcha
import com.google.android.recaptcha.RecaptchaAction
import com.google.android.recaptcha.RecaptchaClient
import com.google.android.recaptcha.RecaptchaException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

object RecaptchaHelper {
    private const val TAG = "reCAPTCHA"
    private const val SITE_KEY = "6Ld85XQrAAAAACMw8m2b885JksCyw5Myb-_I5ZLa"

    private var recaptchaClient: RecaptchaClient? = null
    private val recaptchaScope = CoroutineScope(Dispatchers.IO)

    fun initialize(application: Application) {
        recaptchaScope.launch {
            try {
                recaptchaClient = Recaptcha.fetchClient(application, SITE_KEY)
                Log.d(TAG, "Cliente inicializado")
            } catch (e: RecaptchaException) {
                Log.e(TAG, "Erro ao inicializar: ${e.message}", e)
            }
        }
    }

    private fun executeLoginAction(onTokenReceived: (String?, String?) -> Unit) {
        val client = recaptchaClient ?: return onTokenReceived(null, null)
        recaptchaScope.launch {
            try {
                client.execute(RecaptchaAction.LOGIN)
                    .onSuccess { token ->
                        onTokenReceived(token, SITE_KEY)
                    }
                    .onFailure { exception ->
                        Log.e(TAG, "Erro na execução: ${exception.message}", exception)
                        onTokenReceived(null, null)
                    }
            } catch (e: Exception) {
                Log.e(TAG, "Exceção inesperada: ${e.message}", e)
                onTokenReceived(null, null)
            }
        }
    }

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