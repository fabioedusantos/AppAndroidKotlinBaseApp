package br.com.fbsantos.baseapp.ui.screen.app.notificacoes

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.navigation.NavController
import br.com.fbsantos.baseapp.config.navigation.Routes
import br.com.fbsantos.baseapp.ui.AppViewModel
import br.com.fbsantos.baseapp.ui.components.LogAtividade
import br.com.fbsantos.ui.main.perfil.NotificacoesViewModel
import org.koin.androidx.compose.koinViewModel
import org.koin.compose.koinInject

@Composable
fun NotificacoesScreen(
    navController: NavController,
    viewModel: NotificacoesViewModel = koinViewModel()
) {
    val uiState = viewModel.uiState.collectAsState().value
    val appViewModel: AppViewModel = koinInject()
    val appState = appViewModel.uiState.collectAsState().value

    LogAtividade(Routes.Notificacoes.route, "Notificações", "Notifications")

    NotificacoesContent(
        navController = navController,
        appState = appState,
        onSair = { appViewModel.deslogar() },
        state = uiState,
        onCanalChange = { viewModel.onChannelIdChange(it) },
        onTituloChange = { viewModel.onTitleChange(it) },
        onBodyChange = { viewModel.onBodyChange(it) },
        onLinkChange = { viewModel.onLinkChange(it) },
        onSalvar = { viewModel.onSalvar(it) }
    )
}