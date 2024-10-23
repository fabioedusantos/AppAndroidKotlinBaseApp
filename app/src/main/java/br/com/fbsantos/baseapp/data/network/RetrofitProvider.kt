package br.com.fbsantos.baseapp.data.network

import br.com.fbsantos.baseapp.config.AppConfig
import br.com.fbsantos.baseapp.data.network.authenticator.TokenAuthenticator
import br.com.fbsantos.baseapp.data.network.interceptor.AuthInterceptor
import br.com.fbsantos.baseapp.domain.usecase.configuracoes.TokenManagerUseCase
import br.com.fbsantos.baseapp.util.DebugHttpHelper
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object RetrofitProvider {
    fun provideRetrofit(
        tokenManagerUseCase: TokenManagerUseCase,
        baseUrl: String,
        refreshUrl: String
    ): Retrofit {

        val client = if (AppConfig.IS_DEBUG) {
            DebugHttpHelper.getUnsafeOkHttpClient().newBuilder()
//                .connectTimeout(AppConfig.REQUEST_TIMEOUT_SECONDS, TimeUnit.SECONDS)
                .readTimeout(AppConfig.REQUEST_TIMEOUT_SECONDS, TimeUnit.SECONDS)
                .writeTimeout(AppConfig.REQUEST_TIMEOUT_SECONDS, TimeUnit.SECONDS)
                .addInterceptor(AuthInterceptor(tokenManagerUseCase))
                .authenticator(TokenAuthenticator(tokenManagerUseCase, refreshUrl))
                .build()
        } else {
            OkHttpClient.Builder()
//                .connectTimeout(AppConfig.REQUEST_TIMEOUT_SECONDS, TimeUnit.SECONDS)
                .readTimeout(AppConfig.REQUEST_TIMEOUT_SECONDS, TimeUnit.SECONDS)
                .writeTimeout(AppConfig.REQUEST_TIMEOUT_SECONDS, TimeUnit.SECONDS)
                .addInterceptor(AuthInterceptor(tokenManagerUseCase))
                .authenticator(TokenAuthenticator(tokenManagerUseCase, refreshUrl))
                .build()
        }

        return Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()
    }
}