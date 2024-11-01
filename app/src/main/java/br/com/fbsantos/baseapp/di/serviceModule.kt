package br.com.fbsantos.baseapp.di

import br.com.fbsantos.baseapp.domain.service.PrivateUserService
import br.com.fbsantos.baseapp.domain.service.PublicAuthService
import br.com.fbsantos.baseapp.domain.usecase.configuracoes.TokenManagerUseCase
import org.koin.core.qualifier.named
import org.koin.dsl.module

val serviceModule = module {
    single {
        PublicAuthService(
            get(named("PublicAuthApiService")),
            get<TokenManagerUseCase>()
        )
    }

    single {
        PrivateUserService(get(named("PrivateUserApiService")))
    }
}