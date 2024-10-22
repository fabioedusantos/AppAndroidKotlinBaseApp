package br.com.fbsantos.baseapp.config.navigation

sealed class Routes(val route: String) {
    //auth
    object Login : Routes("login")
    object CriarConta : Routes("criar_conta")
    object RecuperarConta : Routes("recuperar_conta")

    //public
    object PoliticaPrivacidade : Routes("politica_privacidade")
    object TermosCondicoes : Routes("termos_condicoes")

    //app
    object Splash : Routes("splash")
}