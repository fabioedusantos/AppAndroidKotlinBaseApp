package br.com.fbsantos.baseapp.ui.screen.auth.recuperarconta

import br.com.fbsantos.baseapp.config.AppConfig

data class RecuperarContaUiState (
    val etapa: RecuperarContaEtapaEnum = RecuperarContaEtapaEnum.INICIO,

    val email: String = "",
    val isEmailError: Boolean = false,
    val emailErrorText: String = "",

    val segundosRestantes: Int = AppConfig.TEMPO_ESPERA_REENVIO_EMAIL_SEGUNDOS,

    val codigo: List<String> = List(AppConfig.TOTAL_DIGITOS_CODIGO_RECUPERACAO) { "" },
    val isFocarCodigo: Boolean = false,

    val senha: String = "",
    val isSenhaVisivel: Boolean = false,
    val isSenhaError: Boolean = false,
    val senhaErrorText: String = "",

    val confirmeSenha: String = "",
    val isConfirmeSenhaVisivel: Boolean = false,
    val isConfirmeSenhaError: Boolean = false,

    val isFormEnabled: Boolean = true,

    val error: String? = null,
    val messageWait: String? = null
)