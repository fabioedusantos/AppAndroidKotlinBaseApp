package br.com.fbsantos.di

import br.com.fbsantos.baseapp.ui.auth.login.LoginViewModel
import br.com.fbsantos.ui.auth.criarconta.CriarContaViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {

    //singleton scoped (seguem o ciclo de vida composable)
    viewModel{
        LoginViewModel()
    }

    viewModel{
        CriarContaViewModel()
    }
}