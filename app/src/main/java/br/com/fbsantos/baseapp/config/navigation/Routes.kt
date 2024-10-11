package br.com.fbsantos.baseapp.config.navigation

sealed class Routes(val route: String) {
    //auth
    object Login : Routes("login")

    //app
    object Splash : Routes("splash")
}