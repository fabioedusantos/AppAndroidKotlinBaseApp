package br.com.fbsantos.baseapp.util

import android.util.Log
import br.com.fbsantos.baseapp.config.AppConfig
import okhttp3.OkHttpClient
import okhttp3.RequestBody
import okio.Buffer
import retrofit2.Response
import javax.net.ssl.SSLContext
import javax.net.ssl.TrustManager
import javax.net.ssl.X509TrustManager

object DebugHttpHelper {
    const val DEBUG_TAG_REQUEST = "REQUEST"
    
    /**
     * Registra no Logcat os detalhes de uma resposta HTTP do OkHttp, incluindo headers.
     *
     * Deve ser usado quando o corpo da resposta já foi lido externamente
     * para evitar IllegalStateException.
     *
     * @param response Resposta do OkHttp.
     * @param body Corpo da resposta como String (já lido previamente).
     */
    fun log(response: okhttp3.Response, body: String?) {
        if (AppConfig.IS_DEBUG) {
            log(
                method = response.request.method,
                url = response.request.url.toString(),
                headers = formatHeaders(response.request.headers),
                body = response.request.body,
                code = response.code,
                errorBody = body,
                sentRequestAt  = response.sentRequestAtMillis,
                receivedResponseAt = response.receivedResponseAtMillis
            )
        }
    }

    /**
     * Registra no Logcat os detalhes de uma resposta Retrofit, incluindo headers.
     *
     * Lê e exibe o corpo da resposta se disponível. Só deve ser usado
     * se o corpo ainda não foi consumido em outro lugar.
     *
     * @param response Resposta do Retrofit.
     * @param body Corpo da resposta como String (já lido previamente).
     */
    fun <T> log(response: Response<T>, errorBody: String? = null) {
        if (AppConfig.IS_DEBUG) {
            val safeErrorBody = errorBody ?: response.errorBody()?.string()
            log(
                method = response.raw().request.method,
                url = response.raw().request.url.toString(),
                headers = formatHeaders(response.raw().request.headers),
                body = response.raw().request.body,
                code = response.code(),
                errorBody = safeErrorBody,
                sentRequestAt  = response.raw().sentRequestAtMillis,
                receivedResponseAt = response.raw().receivedResponseAtMillis
            )
        }
    }

    /**
     * Método interno que centraliza a lógica de log para requisições HTTP.
     *
     * Apresenta método, URL, headers, corpo da requisição e status da resposta.
     * A resposta é registrada como erro (⛔) para códigos >= 400, e como sucesso (📦) para os demais.
     *
     * @param method Método HTTP (GET, POST, etc).
     * @param url URL completa da requisição.
     * @param headers Cabeçalhos da requisição como string.
     * @param body Corpo da requisição, se aplicável.
     * @param code Código de resposta HTTP.
     * @param errorBody Corpo da resposta como texto, se houver.
     */
    private fun log(
        method: String,
        url: String,
        headers: String,
        body: RequestBody?,
        code: Int,
        errorBody: String?,
        sentRequestAt: Long,
        receivedResponseAt: Long,
    ) {
        val durationMillis = receivedResponseAt - sentRequestAt

        Log.d(
            DEBUG_TAG_REQUEST,
            "=================================== REQUEST ==================================="
        )
        Log.d(DEBUG_TAG_REQUEST, "➡️ $method: $url")
        Log.d(DEBUG_TAG_REQUEST, "➡️ Header: [${headers}]")
        Log.d(DEBUG_TAG_REQUEST, "➡️ Corpo da requisição: ${bodyToString(body)}")
        Log.d(DEBUG_TAG_REQUEST, "🔄 Código de resposta: ${code}")
        Log.d(DEBUG_TAG_REQUEST, "⌛ Tempo da requisição: ${durationMillis}ms")

        if (!errorBody.isNullOrEmpty()) {
            if (code >= 400) {
                Log.e(DEBUG_TAG_REQUEST, "⛔ Resposta: $errorBody")
            } else {
                Log.d(DEBUG_TAG_REQUEST, "📦 Resposta: $errorBody")
            }
        }
    }

    /**
     * Converte os headers de uma requisição HTTP em uma string formatada.
     *
     * Cada par chave-valor é representado como: {Header: Valor}, separado por vírgulas.
     * Útil para exibição legível em logs.
     *
     * @param headers Instância de [okhttp3.Headers] a ser formatada.
     * @return String com os headers formatados, exemplo: {Authorization: Bearer xyz}, {Content-Type: application/json}
     */
    private fun formatHeaders(headers: okhttp3.Headers): String {
        return headers.joinToString(separator = ", ") { "{${it.first}: ${it.second}}" }
    }

    /**
     * Converte o corpo da requisição em texto para exibição no log.
     *
     * @param requestBody Corpo da requisição a ser convertido.
     * @return Representação em texto do corpo, ou mensagem de erro se falhar.
     */
    private fun bodyToString(requestBody: RequestBody?): String {
        return try {
            val buffer = Buffer()
            requestBody?.writeTo(buffer)
            buffer.readUtf8()
        } catch (e: Exception) {
            "Erro ao ler corpo da requisição: ${e.message}"
        }
    }

    /**
     * Cria uma instância de [OkHttpClient] que ignora completamente a validação de certificados HTTPS.
     *
     * ATENÇÃO:
     * Esta função desabilita a verificação SSL e hostname, permitindo conexões com servidores HTTPS
     * que utilizam certificados inválidos, autoassinados ou com domínios incorretos.
     *
     * ➤ Este método deve ser usado apenas para testes locais ou em ambientes de desenvolvimento controlados.
     * ➤ Nunca utilizar em produção, pois expõe a aplicação a ataques man-in-the-middle (MITM).
     *
     * @return [OkHttpClient] configurado para aceitar qualquer certificado SSL e hostname.
     */
    fun getUnsafeOkHttpClient(): OkHttpClient {
        val trustAllCerts = arrayOf<TrustManager>(
            object : X509TrustManager {
                override fun checkClientTrusted(
                    chain: Array<java.security.cert.X509Certificate>,
                    authType: String
                ) {
                }

                override fun checkServerTrusted(
                    chain: Array<java.security.cert.X509Certificate>,
                    authType: String
                ) {
                }

                override fun getAcceptedIssuers(): Array<java.security.cert.X509Certificate> =
                    arrayOf()
            }
        )

        val sslContext = SSLContext.getInstance("SSL")
        sslContext.init(null, trustAllCerts, java.security.SecureRandom())
        val sslSocketFactory = sslContext.socketFactory

        return OkHttpClient.Builder()
            .sslSocketFactory(sslSocketFactory, trustAllCerts[0] as X509TrustManager)
            .hostnameVerifier { _, _ -> true } // ignora nome de host inválido
            .build()
    }
}