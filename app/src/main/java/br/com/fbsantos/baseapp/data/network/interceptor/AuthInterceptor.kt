package br.com.fbsantos.baseapp.data.network.interceptor

import br.com.fbsantos.baseapp.domain.usecase.configuracoes.TokenManagerUseCase
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Response

class AuthInterceptor(
    private val tokenManagerUseCase: TokenManagerUseCase
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val requestBuilder = chain.request().newBuilder()

        runBlocking {
            tokenManagerUseCase.getAuthToken()?.let { token ->
                requestBuilder.addHeader("Authorization", "Bearer $token")
            }
        }

        return chain.proceed(requestBuilder.build())
    }
}