package br.com.fbsantos.baseapp.ui.screen.auth.recuperarconta

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.fbsantos.baseapp.config.AppConfig
import br.com.fbsantos.baseapp.data.network.dto.publicauth.request.ConfirmEmailRequest
import br.com.fbsantos.baseapp.data.network.dto.publicauth.request.ForgotPasswordRequest
import br.com.fbsantos.baseapp.data.network.dto.publicauth.request.ResetPasswordRequest
import br.com.fbsantos.baseapp.domain.service.PublicAuthService
import br.com.fbsantos.baseapp.util.helpers.ValidHelper
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class RecuperarContaViewModel(
    private val publicAuthService: PublicAuthService
) : ViewModel() {

    private val _uiState = MutableStateFlow(RecuperarContaUiState())
    val uiState: StateFlow<RecuperarContaUiState> = _uiState

    fun onEmailChange(email: String) {
        setEmail(email)
        setEmailErrorText(null)
    }

    fun onEnviarCodigo(recaptchaToken: String, recaptchaSiteKey: String) {
        var isValido = true

        if (!ValidHelper.isEmail(_uiState.value.email)) {
            setEmailErrorText("Digite um email válido.")
            isValido = false
        }

        if (!isValido) {
            onClearWaitMessage()
            return
        }

        viewModelScope.launch {
            try {
                publicAuthService.forgotPassword(
                    ForgotPasswordRequest(
                        email = getEmail(),
                        recaptchaToken = recaptchaToken,
                        recaptchaSiteKey = recaptchaSiteKey
                    )
                )

                setEtapa(RecuperarContaEtapaEnum.VALIDAR_CODIGO)
                iniciarContagemReenvioEmail()
                limparCodigo()
            } catch (e: Exception) {
                setError(e.message)
            }
            onClearWaitMessage()
        }
    }

    fun onCodigoChange(codigo: MutableList<String>) {
        setError(null)
        setCodigo(codigo)
    }

    fun onChecarCodigo(recaptchaToken: String, recaptchaSiteKey: String) {
        viewModelScope.launch {
            try {
                publicAuthService.checkResetPassword(
                    ConfirmEmailRequest(
                        email = getEmail(),
                        code = getCodigo(),
                        recaptchaToken = recaptchaToken,
                        recaptchaSiteKey = recaptchaSiteKey
                    )
                )

                setEtapa(RecuperarContaEtapaEnum.ALTERAR_SENHA)
            } catch (e: Exception) {
                setError(e.message)
                limparCodigo()
            }
            onClearWaitMessage()
        }
    }

    fun onReenviarCodigo(recaptchaToken: String, recaptchaSiteKey: String) {
        viewModelScope.launch {
            try {
                publicAuthService.resendConfirmEmail(
                    ForgotPasswordRequest(
                        email = getEmail(),
                        recaptchaToken = recaptchaToken,
                        recaptchaSiteKey = recaptchaSiteKey
                    )
                )
            } catch (e: Exception) {
                setError(e.message)
            }
            iniciarContagemReenvioEmail()
            limparCodigo()
            onClearWaitMessage()
        }
    }

    fun iniciarContagemReenvioEmail() {
        val tempo = AppConfig.TEMPO_ESPERA_REENVIO_EMAIL_SEGUNDOS
        setSegundosRestantes(tempo)

        viewModelScope.launch {
            for (i in tempo downTo 1) {
                kotlinx.coroutines.delay(1000)
                setSegundosRestantes(i - 1)
            }
        }
    }

    fun onSenhaChange(senha: String) {
        setSenha(senha)
        setSenhaErrorText(null)
    }

    fun onSenhaVisivelToggle() {
        setSenhaVisivel(!_uiState.value.isSenhaVisivel)
    }

    fun onConfirmeSenhaChange(confirmeSenha: String) {
        setConfirmeSenha(confirmeSenha)
        setConfirmeSenhaError(false)
    }

    fun onConfirmeSenhaVisivelToggle() {
        setConfirmeSenhaVisivel(!_uiState.value.isConfirmeSenhaVisivel)
    }

    fun onResetarSenha(recaptchaToken: String, recaptchaSiteKey: String) {
        var isValido = true
        setSenhaErrorText(null)
        setError(null)

        if (!ValidHelper.isSenha(getSenha())) {
            setSenhaErrorText(
                "A senha deve ter no mínimo 8 caracteres, 1 letra maiúscula, 1 " +
                        "número e 1 caractere especial."
            )
            isValido = false
        }

        if (getSenha() != getConfirmeSenha()) {
            setConfirmeSenhaError(true)
            setSenhaErrorText("As senhas digitadas não coincidem.")
            isValido = false
        }

        if (!isValido) {
            onClearWaitMessage()
            return
        }

        viewModelScope.launch {
            try {
                publicAuthService.resetPassword(
                    ResetPasswordRequest(
                        email = getEmail(),
                        code = getCodigo(),
                        password = getSenha(),
                        recaptchaToken = recaptchaToken,
                        recaptchaSiteKey = recaptchaSiteKey
                    )
                )

                _uiState.value = _uiState.value.copy(etapa = RecuperarContaEtapaEnum.SUCESSO)
            } catch (e: Exception) {
                setError(e.message)
            }
            onClearWaitMessage()
        }
    }

    fun resetAllErrors() {
        setEmailErrorText(null)
        setSenhaErrorText(null)
        setError(null)
    }

    fun onClearWaitMessage() {
        setFormEnabled(true)
        setMessageWait(null)
    }

    fun onWaitMessage() {
        resetAllErrors()
        setFormEnabled(false)
        setMessageWait("Aguarde...")
    }

    fun setMessageWait(messageWait: String?) {
        _uiState.value = _uiState.value.copy(
            messageWait = messageWait
        )
    }

    fun getEmail(): String {
        return _uiState.value.email
    }

    fun setEmail(email: String) {
        _uiState.value = _uiState.value.copy(email = email)
    }

    fun setEmailErrorText(emailErrorText: String?) {
        _uiState.value = _uiState.value.copy(
            isEmailError = !emailErrorText.isNullOrEmpty(),
            emailErrorText = emailErrorText.orEmpty()
        )
    }

    fun getCodigo(): String {
        return _uiState.value.codigo.joinToString("")
    }

    fun setCodigo(codigo: MutableList<String>?) {
        _uiState.value = _uiState.value.copy(
            codigo = codigo.orEmpty()
        )
    }

    fun getSenha(): String {
        return _uiState.value.senha
    }

    fun setSenha(senha: String) {
        _uiState.value = _uiState.value.copy(senha = senha)
    }

    fun getConfirmeSenha(): String {
        return _uiState.value.confirmeSenha
    }

    fun setConfirmeSenha(confirmeSenha: String) {
        _uiState.value = _uiState.value.copy(confirmeSenha = confirmeSenha)
    }

    fun setSenhaVisivel(isSenhaVisivel: Boolean) {
        _uiState.value = _uiState.value.copy(isSenhaVisivel = isSenhaVisivel)
    }

    fun setConfirmeSenhaVisivel(isConfirmeSenhaVisivel: Boolean) {
        _uiState.value = _uiState.value.copy(isConfirmeSenhaVisivel = isConfirmeSenhaVisivel)
    }

    fun setSenhaErrorText(senhaErrorText: String?) {
        _uiState.value = _uiState.value.copy(
            isSenhaError = !senhaErrorText.isNullOrEmpty(),
            senhaErrorText = senhaErrorText.orEmpty()
        )
    }

    fun setConfirmeSenhaError(isConfirmeSenhaError: Boolean) {
        _uiState.value = _uiState.value.copy(
            isConfirmeSenhaError = isConfirmeSenhaError
        )
    }

    fun setSegundosRestantes(segundosRestantes: Int) {
        _uiState.value = _uiState.value.copy(
            segundosRestantes = segundosRestantes
        )
    }

    fun setFormEnabled(isFormEnabled: Boolean) {
        _uiState.value = _uiState.value.copy(isFormEnabled = isFormEnabled)
    }

    fun setError(error: String?) {
        _uiState.value = _uiState.value.copy(error = error)
    }

    fun setEtapa(etapa: RecuperarContaEtapaEnum) {
        _uiState.value = _uiState.value.copy(
            etapa = etapa
        )
    }

    fun setIsFocarCodigo(isFocarCodigo: Boolean) {
        _uiState.value = _uiState.value.copy(
            isFocarCodigo = isFocarCodigo
        )
    }

    fun limparCodigo() {
        val codigoLimpo = MutableList(AppConfig.TOTAL_DIGITOS_CODIGO_RECUPERACAO) { "" }
        setCodigo(codigoLimpo)
        setIsFocarCodigo(true)
    }
}