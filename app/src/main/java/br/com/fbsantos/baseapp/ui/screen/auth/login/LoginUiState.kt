package br.com.fbsantos.baseapp.ui.screen.auth.login

data class LoginUiState(
    val isFormEnabled: Boolean = false,
    val email: String = "",
    val senha: String = "",
    val isEmailError: Boolean = false,
    val emailErrorText: String = "",
    val isSenhaError: Boolean = false,
    val senhaErrorText: String = "",
    val isSenhaVisivel: Boolean = false,
    val error: String? = null,
    val isLoginEmailSenha: Boolean = false,
    val messageWait: String? = null
)