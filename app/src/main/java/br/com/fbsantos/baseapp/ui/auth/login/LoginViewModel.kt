package br.com.fbsantos.baseapp.ui.auth.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.fbsantos.baseapp.util.ValidHelper
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent

class LoginViewModel() : ViewModel(), KoinComponent {
    private val _uiState = MutableStateFlow(LoginUiState())
    val uiState: StateFlow<LoginUiState> = _uiState

    init {
        setFormEnabled(true)
    }

    fun onEmailChanged(email: String) {
        setEmail(email)
        setEmailErrorText(null)
    }

    fun onSenhaChanged(senha: String) {
        setSenha(senha)
        setSenhaErrorText(null)
    }

    fun onLogin(recaptchaToken: String, recaptchaSiteKey: String) {
        setFormEnabled(false)
        var isValido = true

        _uiState.value = _uiState.value.copy(error = null)

        if (!ValidHelper.isEmail(_uiState.value.email)) {
            setEmailErrorText("Digite um email válido")
            isValido = false
        }

        if (!ValidHelper.isTamanhoSenha(_uiState.value.senha)) {
            setSenhaErrorText("Senha inválida")
            isValido = false
        }

        if (!isValido) {
            setFormEnabled(true)
            return
        }

        viewModelScope.launch {
            try {
                //TODO FAZER LOGIN NO WEBSERVICE
//                email = getEmail(),
//                password = getSenha(),
//                recaptchaToken = recaptchaToken,
//                recaptchaSiteKey = recaptchaSiteKey
            } catch (e: Exception) {
                setError(e.message)
                setFormEnabled(true)
            }
        }
    }

    fun onLoginByGoogle(idToken: String, recaptchaToken: String, recaptchaSiteKey: String) {
        setFormEnabled(false)
        viewModelScope.launch {
            try {
                //TODO FAZER LOGIN NO WEBSERVICE
//                idTokenFirebase = idToken,
//                recaptchaToken = recaptchaToken,
//                recaptchaSiteKey = recaptchaSiteKey
            } catch (e: Exception) {
                setError(e.message)
                setFormEnabled(true)
            }
        }
    }

    fun setLoginEmailSenha(isLoginEmailSenha: Boolean) {
        _uiState.value = _uiState.value.copy(
            isLoginEmailSenha = isLoginEmailSenha
        )
    }

    fun onSenhaVisivelToggle() {
        _uiState.value = _uiState.value.copy(
            isSenhaVisivel = !_uiState.value.isSenhaVisivel
        )
    }

    fun getSenha(): String {
        return _uiState.value.senha
    }

    fun setSenha(senha: String) {
        _uiState.value = _uiState.value.copy(senha = senha)
    }

    fun getEmail(): String {
        return _uiState.value.email
    }

    fun setEmail(email: String) {
        _uiState.value = _uiState.value.copy(email = email)
    }

    fun setFormEnabled(isFormEnabled: Boolean) {
        _uiState.value = _uiState.value.copy(isFormEnabled = isFormEnabled)
    }

    fun setEmailErrorText(emailErrorText: String?) {
        _uiState.value = _uiState.value.copy(
            isEmailError = !emailErrorText.isNullOrEmpty(),
            emailErrorText = emailErrorText.orEmpty()
        )
    }

    fun setSenhaErrorText(senhaErrorText: String?) {
        _uiState.value = _uiState.value.copy(
            isSenhaError = !senhaErrorText.isNullOrEmpty(),
            senhaErrorText = senhaErrorText.orEmpty()
        )
    }

    fun setError(erroLogin: String?) {
        _uiState.value = _uiState.value.copy(
            error = erroLogin ?: "Erro desconhecido, tente novamente mais tarde!"
        )
    }
}