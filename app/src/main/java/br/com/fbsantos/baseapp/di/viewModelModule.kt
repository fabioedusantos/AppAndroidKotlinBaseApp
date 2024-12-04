package br.com.fbsantos.baseapp.di

import br.com.fbsantos.baseapp.ui.AppViewModel
import br.com.fbsantos.baseapp.ui.screen.auth.criarconta.CriarContaViewModel
import br.com.fbsantos.baseapp.ui.screen.auth.login.LoginViewModel
import br.com.fbsantos.baseapp.ui.screen.auth.recuperarconta.RecuperarContaViewModel
import br.com.fbsantos.ui.main.perfil.PerfilViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
    //singleton global
    single {
        AppViewModel(
            buscarDeviceAuthUseCase = get(),
            salvarDeviceAuthUseCase = get(),
            limparConfiguracoesUseCase = get(),
            buscarTemaUseCase = get(),
            salvarTemaUseCase = get(),
            limparHistoricoAtividadesUseCase = get(),
            buscarUltimasAtividadesUseCase = get(),
            salvarHistoricoAtividadeUseCase = get(),
            publicAuthService = get(),
            privateUserService = get(),
        )
    }

    //singleton scoped (seguem o ciclo de vida composable)
    viewModel {
        LoginViewModel(
            publicAuthService = get()
        )
    }

    viewModel {
        CriarContaViewModel(
            publicAuthService = get()
        )
    }

    viewModel {
        RecuperarContaViewModel(
            publicAuthService = get()
        )
    }

    viewModel {
        PerfilViewModel()
    }
}