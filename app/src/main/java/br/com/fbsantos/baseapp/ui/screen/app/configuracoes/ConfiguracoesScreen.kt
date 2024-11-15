package br.com.fbsantos.baseapp.ui.screen.app.configuracoes

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.navigation.NavController
import br.com.fbsantos.baseapp.config.navigation.Routes
import br.com.fbsantos.baseapp.ui.AppViewModel
import br.com.fbsantos.ui.main.configuracoes.ConfiguracoesContent
import org.koin.compose.koinInject

@Composable
fun ConfiguracoesScreen(
    navController: NavController
) {
    val appViewModel: AppViewModel = koinInject()
    val appState = appViewModel.uiState.collectAsState().value

    LaunchedEffect(Unit) {
        appViewModel.addAtividade(Routes.Configuracoes.route, "Configurações", "Settings")
    }

    ConfiguracoesContent(
        navController = navController,
        appState = appState,
        onSair = { appViewModel.deslogar() },
        onToggleTheme = { appViewModel.alterarTemaUsuario(it) },
        onToggleBiometria = { appViewModel.alterarBiometria(it) }
    )
}