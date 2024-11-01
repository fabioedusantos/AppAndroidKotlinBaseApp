package br.com.fbsantos.baseapp.di

import br.com.fbsantos.baseapp.domain.usecase.configuracoes.BuscarDeviceAuthUseCase
import br.com.fbsantos.baseapp.domain.usecase.configuracoes.BuscarTemaUseCase
import br.com.fbsantos.baseapp.domain.usecase.configuracoes.LimparConfiguracoesUseCase
import br.com.fbsantos.baseapp.domain.usecase.configuracoes.SalvarDeviceAuthUseCase
import br.com.fbsantos.baseapp.domain.usecase.configuracoes.TokenManagerUseCase
import br.com.fbsantos.domain.usecase.configuracoes.SalvarTemaUseCase
import org.koin.dsl.module

val useCaseModule = module {
    single { TokenManagerUseCase(get()) }
    single { BuscarDeviceAuthUseCase(get()) }
    single { SalvarDeviceAuthUseCase(get()) }
    single { LimparConfiguracoesUseCase(get()) }
    single { BuscarTemaUseCase(get()) }
    single { SalvarTemaUseCase(get()) }
}