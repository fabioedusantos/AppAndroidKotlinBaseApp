package br.com.fbsantos.baseapp.di

import br.com.fbsantos.baseapp.ui.screen.auth.criarconta.CriarContaViewModel
import br.com.fbsantos.baseapp.ui.screen.auth.login.LoginViewModel
import br.com.fbsantos.baseapp.ui.screen.auth.recuperarconta.RecuperarContaViewModel
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

    viewModel{
        RecuperarContaViewModel()
    }
}