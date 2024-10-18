package br.com.fbsantos.baseapp.ui.screen.auth.recuperarconta

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.fbsantos.baseapp.config.AppConfig
import br.com.fbsantos.baseapp.util.ValidHelper
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class RecuperarContaViewModel() : ViewModel() {

    private val _uiState = MutableStateFlow(RecuperarContaUiState())
    val uiState: StateFlow<RecuperarContaUiState> = _uiState

    fun onEmailChange(email: String) {
        setEmail(email)
        setEmailErrorText(null)
    }

    fun onEnviarCodigo(recaptchaToken: String, recaptchaSiteKey: String) {
        setFormEnabled(false)
        var isValido = true

        if (!ValidHelper.isEmail(_uiState.value.email)) {
            setEmailErrorText("Digite um email válido.")
            isValido = false
        }

        if (!isValido) {
            setFormEnabled(true)
            return
        }

        viewModelScope.launch {
            try {
                //TODO ENVIAR CÓDIGO AO WS
//                email = getEmail(),
//                recaptchaToken = recaptchaToken,
//                recaptchaSiteKey = recaptchaSiteKey

                setEtapa(EtapaRecuperarContaEnum.EMAIL_ENVIADO)
                setError(null)
                iniciarContagemReenvioEmail()
                limparCodigo()
            } catch (e: Exception) {
                setError(e.message)
            }
            setFormEnabled(true)
        }
    }

    fun onCodigoChange(codigo: MutableList<String>) {
        setError(null)
        setCodigo(codigo)
    }

    fun onChecarCodigo(recaptchaToken: String, recaptchaSiteKey: String) {
        setFormEnabled(false)
        viewModelScope.launch {
            try {
                //TODO CHECAR CÓDIGO NO WS
//                email = getEmail(),
//                code = getCodigo(),
//                recaptchaToken = recaptchaToken,
//                recaptchaSiteKey = recaptchaSiteKey

                setEtapa(EtapaRecuperarContaEnum.CODIGO_VALIDADO)
            } catch (e: Exception) {
                setError(e.message)
                limparCodigo()
            }
            setFormEnabled(true)
        }
    }

    fun onReenviarCodigo(recaptchaToken: String, recaptchaSiteKey: String) {
        setFormEnabled(false)
        viewModelScope.launch {
            try {
                //TODO REENVIAR CÓDIGO AO WS
//                email = getEmail(),
//                recaptchaToken = recaptchaToken,
//                recaptchaSiteKey = recaptchaSiteKey
            } catch (e: Exception) {
                setError(e.message)
            }
            iniciarContagemReenvioEmail()
            limparCodigo()
            setFormEnabled(true)
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
        setFormEnabled(false)

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
            setFormEnabled(true)
            return
        }

        viewModelScope.launch {
            try {
                //TODO RESETAR SENHA NO WS
//                email = getEmail(),
//                code = getCodigo(),
//                password = getSenha(),
//                recaptchaToken = recaptchaToken,
//                recaptchaSiteKey = recaptchaSiteKey

                _uiState.value = _uiState.value.copy(etapa = EtapaRecuperarContaEnum.SUCESSO)
            } catch (e: Exception) {
                setError(e.message)
            }
            setFormEnabled(true)
        }
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

    fun setEtapa(etapa: EtapaRecuperarContaEnum) {
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