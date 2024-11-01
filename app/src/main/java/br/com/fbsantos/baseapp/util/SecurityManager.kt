package br.com.fbsantos.baseapp.util

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import br.com.fbsantos.baseapp.config.navigation.Routes
import br.com.fbsantos.baseapp.config.navigation.isRotaAutenticacao
import br.com.fbsantos.baseapp.config.navigation.isRotaPrivada
import br.com.fbsantos.baseapp.ui.AppViewModel
import org.koin.compose.koinInject

object SecurityManager {
    var firstRoute: String = ""

    /**
     * Realiza a validação de segurança para o controle de rotas baseando-se no estado de autenticação do usuário.
     *
     * @param navController O NavController usado para navegar entre as telas.
     */
    @Composable
    fun validate(navController: NavController) {
        val appViewModel: AppViewModel = koinInject()
        val appState by appViewModel.uiState.collectAsState()
        val currentBackStack by navController.currentBackStackEntryAsState()
        val currentRoute = currentBackStack?.destination?.route

        // quando a aplicação for restaurada pelo Android obterá a rota anteriormente aberta.
        LaunchedEffect(navController) {
            val current = navController.currentBackStackEntry?.destination?.route
            if (!current.isNullOrEmpty() && firstRoute.isEmpty()) {
                if (isRotaPrivada(current)) {
                    firstRoute = current
                } else {
                    firstRoute = Routes.Home.route
                }
            }
        }

        LaunchedEffect(appState.isLoggedIn, appState.isReadyLogin, currentRoute) {

            if (currentRoute.isNullOrEmpty()) {
                return@LaunchedEffect
            }

            if (!appState.isReadyLogin) {
                NavHelper.abrir(
                    navController = navController,
                    rotaDestino = Routes.Splash.route,
                    isLimparTudo = true
                )
                return@LaunchedEffect
            }

            if (appState.isLoggedIn && (isRotaAutenticacao(currentRoute) || currentRoute == Routes.Splash.route)) {
                NavHelper.abrir(
                    navController = navController,
                    rotaDestino = firstRoute,
                    isLimparTudo = true
                )
                return@LaunchedEffect
            }

            if (!appState.isLoggedIn && (isRotaPrivada(currentRoute) || currentRoute == Routes.Splash.route)) {
                NavHelper.abrir(
                    navController = navController,
                    rotaDestino = Routes.Login.route,
                    isLimparTudo = true
                )
                return@LaunchedEffect
            }
        }
    }
}