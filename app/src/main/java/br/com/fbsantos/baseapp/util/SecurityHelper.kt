package br.com.fbsantos.baseapp.util

import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import br.com.fbsantos.baseapp.config.navigation.Routes

object SecurityHelper {
    @Composable
    fun validate(navController: NavController) {


        NavHelper.abrir(
            navController = navController,
            rotaDestino = Routes.Login.route,
            isLimparTudo = true
        )
    }
}