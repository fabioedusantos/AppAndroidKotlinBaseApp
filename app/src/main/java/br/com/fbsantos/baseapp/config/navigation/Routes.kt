package br.com.fbsantos.baseapp.config.navigation

sealed class Routes(val route: String) {
    //auth
    object Login : Routes("login")
    object CriarConta : Routes("criar_conta")

    //app
    object Splash : Routes("splash")
}