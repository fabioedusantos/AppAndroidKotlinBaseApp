package br.com.fbsantos.baseapp.config.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import br.com.fbsantos.baseapp.ui.auth.login.LoginScreen
import br.com.fbsantos.ui.auth.criarconta.CriarContaScreen

val rotasAutenticacao = listOf(
    Routes.Login.route,
    Routes.CriarConta.route,
)

fun isRotaAutenticacao(route: String?): Boolean {
    return route != null && rotasAutenticacao.contains(route)
}

fun NavGraphBuilder.registerAuthScreens(navController: NavController) {
    loginScreen(navController)
    criarContaScreen(navController)
}


fun NavGraphBuilder.loginScreen(navController: NavController) {
    composable(Routes.Login.route) {
        LoginScreen(navController = navController)
    }
}

fun NavGraphBuilder.criarContaScreen(navController: NavController) {
    composable(Routes.CriarConta.route) {
        CriarContaScreen(navController = navController)
    }
}