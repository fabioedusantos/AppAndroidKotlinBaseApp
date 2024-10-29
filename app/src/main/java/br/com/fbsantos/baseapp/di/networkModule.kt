package br.com.fbsantos.baseapp.di

import br.com.fbsantos.baseapp.config.AppConfig
import br.com.fbsantos.baseapp.data.network.RetrofitProvider
import br.com.fbsantos.baseapp.data.network.api.PublicAuthApiService
import org.koin.core.qualifier.named
import org.koin.dsl.module

val networkModule = module {
    single(named("PublicAuthApiService")) {
        RetrofitProvider.provideRetrofit(
            tokenManagerUseCase = get(),
            baseUrl = AppConfig.API_BASE_URL,
            refreshUrl = AppConfig.API_TOKEN_REFRESH_URL
        ).create(PublicAuthApiService::class.java)
    }
}