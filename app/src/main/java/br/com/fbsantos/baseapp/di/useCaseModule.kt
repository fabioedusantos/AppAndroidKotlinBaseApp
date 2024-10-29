package br.com.fbsantos.baseapp.di

import br.com.fbsantos.baseapp.domain.usecase.configuracoes.TokenManagerUseCase
import org.koin.dsl.module

val useCaseModule = module {
    single { TokenManagerUseCase(get()) }
}