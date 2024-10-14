package br.com.fbsantos.baseapp.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Brush

object AppGradient {
    @Composable
    fun primaryBackground(): Brush {
        return Brush.verticalGradient(
            colors = listOf(
                MaterialTheme.colorScheme.tertiary,
                MaterialTheme.colorScheme.secondary
            )
        )
    }
}