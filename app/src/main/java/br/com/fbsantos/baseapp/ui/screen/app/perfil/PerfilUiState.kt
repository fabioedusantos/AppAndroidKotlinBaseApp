package br.com.fbsantos.ui.main.perfil

data class PerfilUiState(
    val isEditarPerfil: Boolean = false,
    val isFormEnabled: Boolean = false,
    val error: String? = null,

    val nome: String = "",
    val isNomeError: Boolean = false,
    val nomeErrorText: String = "",

    val sobrenome: String = "",
    val isSobrenomeError: Boolean = false,
    val sobrenomeErrorText: String = "",

    val foto: String = "",
    val isRemoverFoto: Boolean = false,

    val senha: String = "",
    val isSenhaVisivel: Boolean = false,
    val isSenhaError: Boolean = false,

    val confirmeSenha: String = "",
    val isConfirmeSenhaVisivel: Boolean = false,
    val isConfirmeSenhaError: Boolean = false,
    val senhaErrorText: String = "",
)
