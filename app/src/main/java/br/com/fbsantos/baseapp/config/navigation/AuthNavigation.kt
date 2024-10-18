package br.com.fbsantos.baseapp.config.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import br.com.fbsantos.baseapp.ui.screen.auth.login.LoginScreen
import br.com.fbsantos.baseapp.ui.screen.auth.criarconta.CriarContaScreen
import br.com.fbsantos.baseapp.ui.screen.auth.recuperarconta.RecuperarContaScreen

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
    recuperarContaScreen(navController)
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

fun NavGraphBuilder.recuperarContaScreen(navController: NavController) {
    composable(Routes.RecuperarConta.route) {
        RecuperarContaScreen(navController = navController)
    }
}