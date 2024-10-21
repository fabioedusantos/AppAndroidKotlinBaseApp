package br.com.fbsantos.baseapp.config.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import br.com.fbsantos.baseapp.ui.screen.legal.PoliticaPrivacidadeScreen
import br.com.fbsantos.baseapp.ui.screen.splash.SplashScreen

fun NavGraphBuilder.registerPublicScreens(navController: NavController) {
    splashScreen()
    politicaPrivacidadeScreen(navController)
}

fun NavGraphBuilder.splashScreen() {
    composable(Routes.Splash.route) {
        SplashScreen()
    }
}

fun NavGraphBuilder.politicaPrivacidadeScreen(navController: NavController) {
    composable(Routes.PoliticaPrivacidade.route) {
        PoliticaPrivacidadeScreen(navController = navController)
    }
}
