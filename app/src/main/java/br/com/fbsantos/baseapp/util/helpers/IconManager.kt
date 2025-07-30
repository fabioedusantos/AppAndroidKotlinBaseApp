package br.com.fbsantos.baseapp.util.helpers

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.automirrored.filled.Help
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.ui.graphics.vector.ImageVector

/**
 * Helper para gerenciamento centralizado de ícones do Material Design.
 *
 * Fornece um mapeamento entre nomes amigáveis (strings) e ícones do pacote
 * Material Design, facilitando o uso consistente dos mesmos em diferentes
 * partes da aplicação.
 *
 * Características:
 * - Evita repetição de imports e referências a ícones em múltiplos pontos do código.
 * - Permite alteração rápida de ícones apenas editando este helper.
 * - Retorna um ícone padrão de ajuda (`Help`) caso o nome solicitado não exista no mapa.
 *
 * Exemplos de nomes suportados:
 * - `"Home"`
 * - `"Person"`
 * - `"Settings"`
 * - `"Notifications"`
 * - `"Exit"`
 */
object IconManager {
    private val icons = mapOf(
        "Home" to Icons.Default.Home,
        "Person" to Icons.Default.Person,
        "Settings" to Icons.Default.Settings,
        "Notifications" to Icons.Default.Notifications,
        "Exit" to Icons.AutoMirrored.Filled.ExitToApp
    )

    /**
     * Retorna o ícone correspondente ao nome informado.
     *
     * @param name Nome do ícone desejado, conforme definido no mapa [icons].
     * @return O [ImageVector] correspondente ao nome, ou o ícone de ajuda (`Help`)
     *         caso não exista correspondência.
     */
    fun fromName(name: String): ImageVector {
        return icons[name] ?: Icons.AutoMirrored.Filled.Help
    }
}