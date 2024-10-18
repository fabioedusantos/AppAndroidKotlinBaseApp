package br.com.fbsantos.baseapp.config.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import br.com.fbsantos.baseapp.ui.screen.splash.SplashScreen

fun NavGraphBuilder.registerPublicScreens(navController: NavController) {
    splashScreen()
}

fun NavGraphBuilder.splashScreen() {
    composable(Routes.Splash.route) {
        SplashScreen()
    }
}
