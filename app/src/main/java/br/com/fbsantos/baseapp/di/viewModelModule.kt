package br.com.fbsantos.di

import br.com.fbsantos.baseapp.ui.auth.login.LoginViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {

    //singleton scoped (seguem o ciclo de vida composable)
    viewModel{
        LoginViewModel()
    }
}