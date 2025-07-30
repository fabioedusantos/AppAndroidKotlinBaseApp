package br.com.fbsantos.baseapp.util.helpers

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import br.com.fbsantos.baseapp.config.navigation.Routes
import br.com.fbsantos.baseapp.config.navigation.isRotaAutenticacao
import br.com.fbsantos.baseapp.config.navigation.isRotaPrivada
import br.com.fbsantos.baseapp.ui.AppViewModel
import org.koin.compose.koinInject

/**
 * Helper responsável por gerenciar a segurança de navegação no app.
 *
 * O `SecurityManager` garante que o usuário só possa acessar rotas permitidas
 * conforme seu estado de autenticação e readiness.
 *
 * Funcionalidades:
 * - Redireciona usuários não autenticados para rotas públicas (ex.: Login).
 * - Evita que usuários autenticados acessem novamente rotas de autenticação.
 * - Preserva a primeira rota privada acessada antes da autenticação para redirecionamento posterior.
 */
object SecurityManager {
    /**
     * Armazena a primeira rota privada acessada antes da autenticação.
     * Usada para redirecionar o usuário após login bem-sucedido.
     */
    var firstRoute: String = ""

    /**
     * Realiza a validação de segurança para controle de rotas com base no estado de autenticação do usuário.
     *
     * Comportamento:
     * - Ao restaurar a aplicação, determina a rota inicial ([firstRoute]) com base
     *   no destino atual da pilha de navegação.
     * - Monitora mudanças no estado de autenticação (`isLoggedIn`) e readiness (`isReadyLogin`)
     *   para executar redirecionamentos apropriados.
     *
     * Regras:
     * 1. Se o app **não está pronto para login**, redireciona para a Splash.
     * 2. Se o usuário está **logado** e tenta acessar rota de autenticação ou Splash,
     *    redireciona para [firstRoute].
     * 3. Se o usuário **não está logado** e tenta acessar rota privada ou Splash,
     *    redireciona para Login.
     *
     * @param navController O [NavController] usado para controlar a navegação entre telas.
     */
    @Composable
    fun validate(navController: NavController) {
        val appViewModel: AppViewModel = koinInject()
        val appState by appViewModel.uiState.collectAsState()
        val currentBackStack by navController.currentBackStackEntryAsState()
        val currentRoute = currentBackStack?.destination?.route

        // quando a aplicação for restaurada pelo Android obterá a rota anteriormente aberta.
        LaunchedEffect(navController) {
            val current = navController.currentBackStackEntry?.destination?.route
            if (!current.isNullOrEmpty() && firstRoute.isEmpty()) {
                if (isRotaPrivada(current)) {
                    firstRoute = current
                } else {
                    firstRoute = Routes.Home.route
                }
            }
        }

        LaunchedEffect(appState.isLoggedIn, appState.isReadyLogin, currentRoute) {

            if (currentRoute.isNullOrEmpty()) {
                return@LaunchedEffect
            }

            if (!appState.isReadyLogin) {
                Nav.abrir(
                    navController = navController,
                    rotaDestino = Routes.Splash.route,
                    isLimparTudo = true
                )
                return@LaunchedEffect
            }

            if (appState.isLoggedIn && (isRotaAutenticacao(currentRoute) || currentRoute == Routes.Splash.route)) {
                Nav.abrir(
                    navController = navController,
                    rotaDestino = firstRoute,
                    isLimparTudo = true
                )
                return@LaunchedEffect
            }

            if (!appState.isLoggedIn && (isRotaPrivada(currentRoute) || currentRoute == Routes.Splash.route)) {
                Nav.abrir(
                    navController = navController,
                    rotaDestino = Routes.Login.route,
                    isLimparTudo = true
                )
                return@LaunchedEffect
            }
        }
    }
}