package br.com.fbsantos.baseapp.config.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import br.com.fbsantos.baseapp.ui.screen.app.HomeScreen

val rotasPrivadas = listOf(
    Routes.Home.route,
)

fun isRotaPrivada(route: String?): Boolean {
    return route != null && rotasPrivadas.contains(route)
}

fun NavGraphBuilder.registerPrivateScreens(navController: NavController) {
    homeScreen(navController)
}

fun NavGraphBuilder.homeScreen(navController: NavController) {
    composable(Routes.Home.route) {
        HomeScreen(navController)
    }
}
