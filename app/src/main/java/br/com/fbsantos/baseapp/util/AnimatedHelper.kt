package br.com.fbsantos.baseapp.util

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

object AnimatedHelper {
    private val defaultTimeMs: Int = 700

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

    private fun slideTransition(
        durationMillis: Int
    ): ContentTransform {
        return fadeIn(animationSpec = tween(durationMillis)) togetherWith
                fadeOut(animationSpec = tween(durationMillis))
    }

    fun slideNavTransition(
        durationMillis: Int = defaultTimeMs
    ): AnimatedContentTransitionScope<*>.() -> EnterTransition {
        return {
            fadeIn(animationSpec = tween(durationMillis))
        }
    }

    fun slideNavExitTransition(
        durationMillis: Int = defaultTimeMs
    ): AnimatedContentTransitionScope<*>.() -> ExitTransition {
        return {
            fadeOut(animationSpec = tween(durationMillis))
        }
    }
}