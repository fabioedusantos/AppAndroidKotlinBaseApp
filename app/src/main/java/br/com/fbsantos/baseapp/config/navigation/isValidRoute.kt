package br.com.fbsantos.baseapp.config.navigation

fun isValidRoute(route: String): Boolean {
    val validRoutes = Routes::class.sealedSubclasses
        .mapNotNull { it.objectInstance?.route }

    // Remove parâmetros e query strings
    val routeBase = route.substringBefore("?").substringBefore("/")

    return validRoutes.any { routeBase.startsWith(it) }
}