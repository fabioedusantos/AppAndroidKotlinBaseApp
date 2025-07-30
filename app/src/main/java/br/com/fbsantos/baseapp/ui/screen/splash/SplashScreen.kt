package br.com.fbsantos.baseapp.ui.screen.splash

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.platform.LocalContext
import br.com.fbsantos.baseapp.ui.AppViewModel
import br.com.fbsantos.baseapp.util.helpers.FirebaseNotification
import br.com.fbsantos.baseapp.util.permissions.PermNotification
import org.koin.compose.koinInject

@Composable
fun SplashScreen() {
    val appViewModel: AppViewModel = koinInject()
    val appState = appViewModel.uiState.collectAsState().value
    val context = LocalContext.current

    SplashContent(
        appState = appState,
        onLogin = {
            appViewModel.initializeLogin()
            if (PermNotification.isGranted(context)) {
                FirebaseNotification.subscribeToAll(context)
            }
        },
        onSair = { appViewModel.deslogar() }
    )
}