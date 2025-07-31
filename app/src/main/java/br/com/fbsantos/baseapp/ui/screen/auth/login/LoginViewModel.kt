package br.com.fbsantos.baseapp.ui.screen.auth.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.fbsantos.baseapp.data.network.dto.publicauth.request.LoginGoogleRequest
import br.com.fbsantos.baseapp.data.network.dto.publicauth.request.LoginRequest
import br.com.fbsantos.baseapp.domain.service.PublicAuthService
import br.com.fbsantos.baseapp.ui.AppViewModel
import br.com.fbsantos.baseapp.util.helpers.ValidHelper
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class LoginViewModel(
    private val publicAuthService: PublicAuthService
) : ViewModel(), KoinComponent {
    private val _uiState = MutableStateFlow(LoginUiState())
    val uiState: StateFlow<LoginUiState> = _uiState

    private val appViewModel: AppViewModel by inject()

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
            onClearWaitMessage()
            return
        }

        viewModelScope.launch {
            try {
                publicAuthService.login(
                    LoginRequest(
                        email = getEmail(),
                        password = getSenha(),
                        recaptchaToken = recaptchaToken,
                        recaptchaSiteKey = recaptchaSiteKey
                    )
                )

                appViewModel.initializeSplashScreenLogin()
            } catch (e: Exception) {
                setError(e.message)
            }
            onClearWaitMessage()
        }
    }

    fun onLoginByGoogle(idToken: String, recaptchaToken: String, recaptchaSiteKey: String) {
        viewModelScope.launch {
            try {
                publicAuthService.loginGoogle(
                    LoginGoogleRequest(
                        idTokenFirebase = idToken,
                        recaptchaToken = recaptchaToken,
                        recaptchaSiteKey = recaptchaSiteKey
                    )
                )

                appViewModel.initializeSplashScreenLogin()
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

    fun setError(error: String?) {
        _uiState.value = _uiState.value.copy(
            error = error
        )
    }
}