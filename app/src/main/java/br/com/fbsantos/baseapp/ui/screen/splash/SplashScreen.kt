package br.com.fbsantos.baseapp.ui.screen.splash

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import br.com.fbsantos.baseapp.ui.AppViewModel
import org.koin.compose.koinInject

@Composable
fun SplashScreen() {
    val appViewModel: AppViewModel = koinInject()
    val appState = appViewModel.uiState.collectAsState().value

    SplashContent(
        appState = appState,
        onLogin = { appViewModel.initializeLogin() },
        onSair = { appViewModel.deslogar() }
    )
}