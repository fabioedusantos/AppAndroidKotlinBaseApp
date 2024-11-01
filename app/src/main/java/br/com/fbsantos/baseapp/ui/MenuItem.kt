package br.com.fbsantos.ui.app.model

import androidx.compose.ui.graphics.vector.ImageVector

data class MenuItem(
    val title: String,
    val icon: ImageVector,
    val routeOpen: String
)
