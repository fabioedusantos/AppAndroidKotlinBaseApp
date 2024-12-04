package br.com.fbsantos.baseapp.ui.screen.app.perfil

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.navigation.NavController
import br.com.fbsantos.baseapp.config.navigation.Routes
import br.com.fbsantos.baseapp.ui.AppViewModel
import br.com.fbsantos.baseapp.ui.screen.app.perfil.content.PerfilEditContent
import br.com.fbsantos.baseapp.ui.screen.app.perfil.content.PerfilViewContent
import br.com.fbsantos.ui.main.perfil.PerfilViewModel
import org.koin.androidx.compose.koinViewModel
import org.koin.compose.koinInject

@Composable
fun PerfilScreen(
    navController: NavController,
    viewModel: PerfilViewModel = koinViewModel()
) {
    val uiState = viewModel.uiState.collectAsState().value
    val appViewModel: AppViewModel = koinInject()
    val appState = appViewModel.uiState.collectAsState().value

    LaunchedEffect(Unit) {
        appViewModel.addAtividade(Routes.Perfil.route, "Perfil", "Person")
    }

    if (!uiState.isEditarPerfil) {
        PerfilViewContent(
            navController = navController,
            appState = appState,
            onSair = { appViewModel.deslogar() },
            onToggleEditarPerfil = { viewModel.onToggleEditarPerfil() }
        )
    } else {
        PerfilEditContent(
            navController = navController,
            appState = appState,
            onSair = { appViewModel.deslogar() }
        )
    }


}