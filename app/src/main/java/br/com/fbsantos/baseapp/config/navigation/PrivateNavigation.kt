package br.com.fbsantos.baseapp.config.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import br.com.fbsantos.baseapp.ui.screen.app.configuracoes.ConfiguracoesScreen
import br.com.fbsantos.baseapp.ui.screen.app.home.HomeScreen

val rotasPrivadas = listOf(
    Routes.Home.route,
    Routes.Configuracoes.route,
)

fun isRotaPrivada(route: String?): Boolean {
    return route != null && rotasPrivadas.contains(route)
}

fun NavGraphBuilder.registerPrivateScreens(navController: NavController) {
    homeScreen(navController)
    configuracoesScreen(navController)
}

fun NavGraphBuilder.homeScreen(navController: NavController) {
    composable(Routes.Home.route) {
        HomeScreen(navController)
    }
}

fun NavGraphBuilder.configuracoesScreen(navController: NavController) {
    composable(Routes.Configuracoes.route) {
        ConfiguracoesScreen(navController)
    }
}