package br.com.fbsantos.baseapp.config.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import br.com.fbsantos.baseapp.ui.auth.login.LoginScreen

val rotasAutenticacao = listOf(
    Routes.Login.route
)

fun isRotaAutenticacao(route: String?): Boolean {
    return route != null && rotasAutenticacao.contains(route)
}

fun NavGraphBuilder.registerAuthScreens(navController: NavController) {
    loginScreen(navController)
}


fun NavGraphBuilder.loginScreen(navController: NavController) {
    composable(Routes.Login.route) {
        LoginScreen(navController = navController)
    }
}