package br.com.fbsantos.baseapp.ui.screen.auth.criarconta

import br.com.fbsantos.baseapp.config.AppConfig

data class CriarContaUiState (
    val isFormEnabled: Boolean = true,

    val nome: String = "",
    val isNomeError: Boolean = false,
    val nomeErrorText: String = "",

    val sobrenome: String = "",
    val isSobrenomeError: Boolean = false,
    val sobrenomeErrorText: String = "",

    val email: String = "",
    val isEmailError: Boolean = false,
    val emailErrorText: String = "",
    val isEmailEnabled: Boolean = true,

    val senha: String = "",
    val isSenhaVisivel: Boolean = false,
    val isSenhaError: Boolean = false,
    val isSenhasInputVisible: Boolean = true,

    val confirmeSenha: String = "",
    val isConfirmeSenhaVisivel: Boolean = false,
    val isConfirmeSenhaError: Boolean = false,
    val senhaErrorText: String = "",

    val isTermosAceitos: Boolean = false,
    val isPoliticaAceita: Boolean = false,

    val codigo: List<String> = List(AppConfig.TOTAL_DIGITOS_CODIGO_RECUPERACAO) { "" },
    val isFocarCodigo: Boolean = false,

    val segundosRestantes: Int = AppConfig.TEMPO_ESPERA_REENVIO_EMAIL_SEGUNDOS,

    val etapa: EtapaCriarContaEnum = EtapaCriarContaEnum.INICIAL,

    val isGoogleButtonEnabled: Boolean = true,

    val error: String? = null,
)