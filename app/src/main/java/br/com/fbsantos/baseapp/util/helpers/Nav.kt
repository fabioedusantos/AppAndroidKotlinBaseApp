package br.com.fbsantos.baseapp.util.helpers

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.navigation.NavController

/**
 * Helper para navegação com Jetpack Navigation.
 *
 * Centraliza funções de navegação para evitar código repetido
 * e facilitar ações condicionais e limpeza da pilha de navegação.
 *
 * Funcionalidades:
 * - Navegar condicionalmente a partir de um estado ou evento de UI.
 * - Executar navegação com limpeza parcial ou total da pilha de rotas.
 */
object Nav {
    /**
     * Navega para uma rota se a condição for verdadeira.
     *
     * @param navController Controller de navegação usado para navegar entre as telas.
     * @param condicao Condição booleana que define se a navegação deve ocorrer.
     * @param rotaDestino Rota para onde deve navegar.
     * @param limparAteRota Se definido, remove as rotas anteriores até essa, respeitando o valor de [inclusive].
     * @param inclusive Se true, remove também a rota [limparAteRota]; caso false, ela permanece na pilha.
     */
    @Composable
    fun abrirSe(
        navController: NavController,
        condicao: Boolean,
        rotaDestino: String,
        limparAteRota: String? = null,
        inclusive: Boolean = true
    ) {
        LaunchedEffect(condicao) {
            if (condicao) {
                abrir(navController, rotaDestino, limparAteRota, inclusive)
            }
        }
    }

    /**
     * Executa a navegação para uma rota com a opção de limpar a pilha de navegação.
     *
     * @param navController Controller de navegação usado para a ação.
     * @param rotaDestino Rota de destino.
     * @param limparAteRota Se definido, remove as rotas anteriores até essa, respeitando o valor de [isInclusive].
     * @param isInclusive Se true, remove também a rota [limparAteRota]; caso false, ela permanece na pilha.
     */
    fun abrir(
        navController: NavController,
        rotaDestino: String,
        limparAteRota: String? = null,
        isInclusive: Boolean = true,
        isLimparTudo: Boolean = false
    ) {
        if (navController.currentDestination?.route == rotaDestino) {
            return // já estamos na tela de destino, não navega novamente
        }

        if (isLimparTudo) {
            navController.navigate(rotaDestino) {
                popUpTo(navController.graph.startDestinationId) {
                    this.inclusive = isInclusive
                }
            }
            return
        }

        if (limparAteRota == null) {
            navController.navigate(rotaDestino)
        } else {
            navController.navigate(rotaDestino) {
                limparAteRota.let {
                    popUpTo(it) {
                        // Remove todas as rotas da pilha até alcançar 'limparAteRota'.
                        // Se 'inclusive' for true, a própria rota 'limparAteRota' também será removida.
                        this.inclusive = isInclusive
                    }
                }
            }
        }
    }
}