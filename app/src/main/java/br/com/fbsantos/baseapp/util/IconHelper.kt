package br.com.fbsantos.baseapp.util

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Help
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.ui.graphics.vector.ImageVector

object IconHelper {
    private val icons = mapOf(
        "Home" to Icons.Default.Home,
        "Person" to Icons.Default.Person,
        "Settings" to Icons.Default.Settings
    )

    fun fromName(name: String): ImageVector {
        return icons[name] ?: Icons.AutoMirrored.Filled.Help
    }
}