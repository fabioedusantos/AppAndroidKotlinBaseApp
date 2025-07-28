package br.com.fbsantos.baseapp.config.navigation

fun isValidRoute(route: String): Boolean {
    val validRoutes = Routes::class.sealedSubclasses.mapNotNull { it.objectInstance?.route }

    val routeBase = route
        .substringBefore("?")    // remove query strings
        .split("/")            // quebra em segmentos
        .firstOrNull()                   // pega só o primeiro segmento
        ?: return false

    return validRoutes.any { routeBase == it }
}