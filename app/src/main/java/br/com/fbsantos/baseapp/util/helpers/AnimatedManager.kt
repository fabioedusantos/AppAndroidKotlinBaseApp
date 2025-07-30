package br.com.fbsantos.baseapp.util.helpers

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedContentScope
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.ContentTransform
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.runtime.Composable

/**
 * Helper responsável por centralizar e padronizar animações
 * de transição de conteúdo no Jetpack Compose.
 *
 * Fornece métodos reutilizáveis para aplicar efeitos
 * como fadeIn/fadeOut em mudanças de estado e navegação.
 */
object AnimatedManager {
    private val defaultTimeMs: Int = 700

    /**
     * Alterna entre diferentes conteúdos com animação, baseado no estado alvo.
     *
     * @param targetState Estado que dispara a troca de conteúdo.
     * @param durationMillis Duração da animação em milissegundos (padrão: [defaultTimeMs]).
     * @param content Composable que renderiza o conteúdo de acordo com o estado.
     */
    @Composable
    fun <T> Switcher(
        targetState: T,
        durationMillis: Int = defaultTimeMs,
        content: @Composable AnimatedContentScope.(T) -> Unit
    ) {
        AnimatedContent(
            targetState = targetState,
            transitionSpec = {
                slideTransition(durationMillis)
            },
            content = content
        )
    }

    /**
     * Cria uma transição padrão com fadeIn (entrada) e fadeOut (saída).
     *
     * @param durationMillis Duração da animação em milissegundos.
     * @return Um [ContentTransform] que combina entrada e saída.
     */
    private fun slideTransition(
        durationMillis: Int
    ): ContentTransform {
        return fadeIn(animationSpec = tween(durationMillis)) togetherWith
                fadeOut(animationSpec = tween(durationMillis))
    }

    /**
     * Retorna a transição padrão de entrada para navegação (fadeIn).
     *
     * @param durationMillis Duração da animação em milissegundos.
     * @return Função que descreve a animação de entrada.
     */
    fun slideNavTransition(
        durationMillis: Int = defaultTimeMs
    ): AnimatedContentTransitionScope<*>.() -> EnterTransition {
        return {
            fadeIn(animationSpec = tween(durationMillis))
        }
    }

    /**
     * Retorna a transição padrão de saída para navegação (fadeOut).
     *
     * @param durationMillis Duração da animação em milissegundos.
     * @return Função que descreve a animação de saída.
     */
    fun slideNavExitTransition(
        durationMillis: Int = defaultTimeMs
    ): AnimatedContentTransitionScope<*>.() -> ExitTransition {
        return {
            fadeOut(animationSpec = tween(durationMillis))
        }
    }
}