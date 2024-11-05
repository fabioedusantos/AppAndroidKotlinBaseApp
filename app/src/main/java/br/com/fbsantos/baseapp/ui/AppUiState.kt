package br.com.fbsantos.ui.app

import br.com.fbsantos.baseapp.data.database.HistoricoAtividadeEntity
import br.com.fbsantos.ui.app.model.MenuItem
import java.util.Date

data class AppUiState(
    val isReadyDb: Boolean = false,
    val isReadyLogin: Boolean = false,
    val isLoggedIn: Boolean = false,

    val nome: String = "Carregando...",
    val sobrenome: String = "",
    val email: String = "Carregando...",
    val fotoBlob: String = "",
    val ultimoAcesso: Date? = null,
    val criacao: Date? = null,
    val alteracao: Date? = null,
    val isContaGoogle: Boolean = false,

    val numeroMensagens: Int = 12,
    val numeroEventosDia: Int = 2,
    val historicoAtividade: List<HistoricoAtividadeEntity> = listOf(),

    val isDarkTheme: Boolean? = null,
    val isDeviceAuthEnabled: Boolean = false,
    val menuItems: List<MenuItem> = emptyList()
) {
    val nomeCompleto: String
        get() {
            val fullName = listOf(nome, sobrenome)
                .filter { it.isNotBlank() && it != "Carregando..." }
                .joinToString(" ")
            return if (fullName.isBlank()) "Carregando..." else fullName
        }
}
