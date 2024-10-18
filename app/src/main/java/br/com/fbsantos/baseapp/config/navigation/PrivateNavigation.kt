package br.com.fbsantos.baseapp.config.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder

val rotasPrivadas = listOf("")

fun isRotaPrivada(route: String?): Boolean {
    return route != null && rotasPrivadas.contains(route)
}

fun NavGraphBuilder.registerPrivateScreens(navController: NavController) {

}
