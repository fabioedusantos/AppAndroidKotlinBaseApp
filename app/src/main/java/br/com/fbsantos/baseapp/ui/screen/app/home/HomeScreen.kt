package br.com.fbsantos.baseapp.ui.screen.app.home

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.navigation.NavController
import br.com.fbsantos.baseapp.config.navigation.Routes
import br.com.fbsantos.baseapp.ui.AppViewModel
import org.koin.compose.koinInject

@Composable
fun HomeScreen(
    navController: NavController
) {
    val appViewModel: AppViewModel = koinInject()
    val appState = appViewModel.uiState.collectAsState().value

    LaunchedEffect(Unit) {
        appViewModel.addAtividade(Routes.Home.route, "Home", "Home")
    }

    HomeContent(
        navController = navController,
        appState = appState,
        onSair = { appViewModel.deslogar() }
    )
}