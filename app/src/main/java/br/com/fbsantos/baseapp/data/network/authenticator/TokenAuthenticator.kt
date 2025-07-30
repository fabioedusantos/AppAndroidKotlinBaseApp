package br.com.fbsantos.baseapp.data.network.authenticator

import br.com.fbsantos.baseapp.config.AppConfig
import br.com.fbsantos.baseapp.domain.usecase.configuracoes.TokenManagerUseCase
import br.com.fbsantos.baseapp.util.helpers.DebugHttp
import br.com.fbsantos.baseapp.util.helpers.Utils
import kotlinx.coroutines.runBlocking
import okhttp3.Authenticator
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.Response
import okhttp3.Route
import org.json.JSONObject

class TokenAuthenticator(
    private val tokenManagerUseCase: TokenManagerUseCase,
    private val refreshUrl: String
) : Authenticator {

    override fun authenticate(route: Route?, response: Response): Request? {
        if (Utils.isRunningInPreview()) return null //para não gerar erros no preview
        if (responseCount(response) >= 2) return null

        return runBlocking {
            try {
                val refreshToken = tokenManagerUseCase.getRefreshToken()
                if (refreshToken.isNullOrBlank()) return@runBlocking null //se for nula ou vazia não faz a request

                val body = JSONObject().put("refresh_token", refreshToken).toString()
                    .toRequestBody("application/json".toMediaTypeOrNull())

                val request = Request.Builder()
                    .url(refreshUrl)
                    .post(body)
                    .build()

                val client = if (AppConfig.IS_DEBUG) {
                    DebugHttp.getUnsafeOkHttpClient()
                } else {
                    OkHttpClient()
                }
                val refreshResponse = client.newCall(request).execute()
                val responseBody = refreshResponse.body?.string()

                DebugHttp.log(refreshResponse, responseBody)

                if (refreshResponse.isSuccessful) {
                    val json = JSONObject(responseBody.orEmpty())
                    if (json.getString("status").equals("success")) {

                        val data = json.optJSONObject("data")
                        val newToken = data?.getString("token")
                        val newRefreshToken = data?.getString("refreshToken")

                        if (!newToken.isNullOrBlank() && !newRefreshToken.isNullOrBlank()) {
                            tokenManagerUseCase.salvarTokens(newToken, newRefreshToken)
                            return@runBlocking response.request.newBuilder()
                                .header("Authorization", "Bearer $newToken")
                                .build()
                        }

                    }
                }

                tokenManagerUseCase.limparTokens()
                null
            } catch (e: Exception) {
                tokenManagerUseCase.limparTokens()
                null
            }
        }
    }

    private fun responseCount(response: Response): Int {
        var count = 1
        var prior = response.priorResponse
        while (prior != null) {
            count++
            prior = prior.priorResponse
        }
        return count
    }
}