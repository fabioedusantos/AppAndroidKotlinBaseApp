package br.com.fbsantos.baseapp.ui.screen.auth.criarconta

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.fbsantos.baseapp.config.AppConfig
import br.com.fbsantos.baseapp.data.network.dto.publicauth.request.ConfirmEmailRequest
import br.com.fbsantos.baseapp.data.network.dto.publicauth.request.ForgotPasswordRequest
import br.com.fbsantos.baseapp.data.network.dto.publicauth.request.SignupGoogleRequest
import br.com.fbsantos.baseapp.data.network.dto.publicauth.request.SignupRequest
import br.com.fbsantos.baseapp.domain.service.PublicAuthService
import br.com.fbsantos.baseapp.util.FirebaseAuthHelper
import br.com.fbsantos.baseapp.util.ToastManager
import br.com.fbsantos.baseapp.util.UtilsHelper
import br.com.fbsantos.baseapp.util.ValidHelper
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class CriarContaViewModel(
    private val publicAuthService: PublicAuthService
) : ViewModel() {

    private val _uiState = MutableStateFlow(CriarContaUiState())
    val uiState: StateFlow<CriarContaUiState> = _uiState
    private var idTokenFirebase: String? = null

    fun onNomeChange(nome: String) {
        setNome(nome)
        setNomeErrorText(null)
    }

    fun onSobreNomeChange(sobreNome: String) {
        setSobrenome(sobreNome)
        setSobrenomeErrorText(null)
    }

    fun onEmailChange(email: String) {
        setEmail(email)
        setEmailErrorText(null)
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

    fun onTermosAceitosChange(isTermosAceitos: Boolean) {
        setTermosAceitos(isTermosAceitos)
    }

    fun onPoliticaAceitaChange(isPoliticaAceita: Boolean) {
        setPoliticaAceita(isPoliticaAceita)
    }

    fun isNomeValido(): Boolean {
        if (getNome().trim().length < 2) {
            setNomeErrorText("Digite ao menos 2 letras para o nome.")
            return false
        }
        return true
    }

    fun isSobrenomeValido(): Boolean {
        if (getSobrenome().trim().length < 2) {
            setSobrenomeErrorText("Digite ao menos 2 letras para o sobrenome.")
            return false
        }
        return true
    }

    fun isTermosValidos(): Boolean {
        if (!getTermosAceitos() || !getPoliticaAceita()) {
            setError("Você precisa aceitar os termos e a política de privacidade.")
            return false
        }
        return true
    }

    fun onCriarConta(recaptchaToken: String, recaptchaSiteKey: String) {
        if (idTokenFirebase == null) {
            criarContaDefault(recaptchaToken, recaptchaSiteKey)
        } else {
            criarContaGoogle(recaptchaToken, recaptchaSiteKey)
        }
    }

    private fun criarContaDefault(recaptchaToken: String, recaptchaSiteKey: String) {
        setFormEnabled(false)
        resetAllErrors()

        var isValido = true

        if (!isNomeValido()) {
            isValido = false
        }

        if (!isSobrenomeValido()) {
            isValido = false
        }

        if (!ValidHelper.isEmail(getEmail())) {
            setEmailErrorText("Digite um email válido.")
            isValido = false
        }

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

        if (!isTermosValidos()) {
            isValido = false
        }

        if (!isValido) {
            setFormEnabled(true)
            return
        }

        viewModelScope.launch {
            try {
                setFormEnabled(false)
                publicAuthService.signup(
                    SignupRequest(
                        name = getNome(),
                        lastname = getSobrenome(),
                        email = getEmail(),
                        password = getSenha(),
                        isTerms = getTermosAceitos(),
                        isPolicy = getPoliticaAceita(),
                        recaptchaToken = recaptchaToken,
                        recaptchaSiteKey = recaptchaSiteKey
                    )
                )

                setEtapa(EtapaCriarContaEnum.EMAIL_ENVIADO)
                setError(null)
                iniciarContagemReenvioEmail()
                limparCodigo()
            } catch (e: Exception) {
                setError(e.message)
            }
            setFormEnabled(true)
        }
    }

    fun onCriarContaByGoogle(idTokenFirebase: String, userFirebase: FirebaseUser) {
        setFormEnabled(false)

        setGoogleButtonEnabled(false)
        setSenhasInputVisible(false)
        setEmailEnabled(false)

        val (nome, sobrenome) = UtilsHelper.separarNomeSobrenome(userFirebase.displayName)
        val email = userFirebase.email

        if (nome != null) setNome(nome)
        if (sobrenome != null) setSobrenome(sobrenome)
        if (email != null) setEmail(email)

        //sem email valido? como isso? vai que...
        if (!ValidHelper.isEmail(getEmail())) {
            ToastManager.show("Não é possível prosseguir. Por favor crie sua conta manualmente!")
            resetForm()
            return
        }

        this.idTokenFirebase = idTokenFirebase
        setFormEnabled(true)
    }

    private fun criarContaGoogle(recaptchaToken: String, recaptchaSiteKey: String) {
        resetAllErrors()
        var isValido = true

        if (!isNomeValido()) {
            isValido = false
        }

        if (!isSobrenomeValido()) {
            isValido = false
        }

        if (!isTermosValidos()) {
            isValido = false
        }

        if (!isValido) {
            setFormEnabled(true)
            return
        }

        viewModelScope.launch {
            try {
                setFormEnabled(false)
                publicAuthService.signUpGoogle(
                    SignupGoogleRequest(
                        idTokenFirebase = idTokenFirebase ?: "",
                        name = getNome(),
                        lastname = getSobrenome(),
                        isTerms = getTermosAceitos(),
                        isPolicy = getPoliticaAceita(),
                        recaptchaToken = recaptchaToken,
                        recaptchaSiteKey = recaptchaSiteKey
                    )
                )

                setEtapa(EtapaCriarContaEnum.CODIGO_VALIDADO)
            } catch (e: Exception) {
                setError(e.message)
            }
            setFormEnabled(true)
        }
    }

    fun resetForm() {
        setNome("")
        setSobrenome("")
        setEmail("")
        setSenha("")
        setConfirmeSenha("")
        setTermosAceitos(false)
        setPoliticaAceita(false)

        setGoogleButtonEnabled(true)
        setEmailEnabled(true)
        setSenhasInputVisible(true)

        resetAllErrors()
        idTokenFirebase = null
        FirebaseAuthHelper.logout()
        setFormEnabled(true)
    }

    fun resetAllErrors() {
        setNomeErrorText(null)
        setSobrenomeErrorText(null)
        setEmailErrorText(null)
        setSenhaErrorText(null)
        setError(null)
    }

    fun onCodigoChange(codigo: MutableList<String>) {
        setError(null)
        setCodigo(codigo)
    }

    fun onChecarCodigo(recaptchaToken: String, recaptchaSiteKey: String) {
        setFormEnabled(false)
        viewModelScope.launch {
            try {
                publicAuthService.confirmEmail(
                    ConfirmEmailRequest(
                        email = getEmail(),
                        code = getCodigo(),
                        recaptchaToken = recaptchaToken,
                        recaptchaSiteKey = recaptchaSiteKey
                    )
                )

                setEtapa(EtapaCriarContaEnum.CODIGO_VALIDADO)
            } catch (e: Exception) {
                setError(e.message)
                setFormEnabled(true)
                limparCodigo()
            }
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

    fun onReenviarCodigo(recaptchaToken: String, recaptchaSiteKey: String) {
        setFormEnabled(false)
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
            setFormEnabled(true)
        }
    }

    fun getNome(): String {
        return _uiState.value.nome
    }

    fun setNome(nome: String) {
        _uiState.value = _uiState.value.copy(nome = nome)
    }

    fun getSobrenome(): String {
        return _uiState.value.sobrenome
    }

    fun setSobrenome(sobrenome: String) {
        _uiState.value = _uiState.value.copy(sobrenome = sobrenome)
    }

    fun getEmail(): String {
        return _uiState.value.email
    }

    fun setEmail(email: String) {
        _uiState.value = _uiState.value.copy(email = email)
    }

    fun setEmailEnabled(isEmailEnabled: Boolean) {
        _uiState.value = _uiState.value.copy(isEmailEnabled = isEmailEnabled)
    }

    fun getSenha(): String {
        return _uiState.value.senha
    }

    fun setSenha(senha: String) {
        _uiState.value = _uiState.value.copy(senha = senha)
    }

    fun setSenhasInputVisible(isSenhasInputVisible: Boolean) {
        _uiState.value = _uiState.value.copy(isSenhasInputVisible = isSenhasInputVisible)
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

    fun getTermosAceitos(): Boolean {
        return _uiState.value.isTermosAceitos
    }

    fun setTermosAceitos(isTermosAceitos: Boolean) {
        _uiState.value = _uiState.value.copy(isTermosAceitos = isTermosAceitos)
    }

    fun getPoliticaAceita(): Boolean {
        return _uiState.value.isPoliticaAceita
    }

    fun setPoliticaAceita(isPoliticaAceita: Boolean) {
        _uiState.value = _uiState.value.copy(isPoliticaAceita = isPoliticaAceita)
    }

    fun setNomeErrorText(nomeErrorText: String?) {
        _uiState.value = _uiState.value.copy(
            isNomeError = !nomeErrorText.isNullOrEmpty(),
            nomeErrorText = nomeErrorText.orEmpty()
        )
    }

    fun setSobrenomeErrorText(sobrenomeErrorText: String?) {
        _uiState.value = _uiState.value.copy(
            isSobrenomeError = !sobrenomeErrorText.isNullOrEmpty(),
            sobrenomeErrorText = sobrenomeErrorText.orEmpty()
        )
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

    fun setConfirmeSenhaError(isConfirmeSenhaError: Boolean) {
        _uiState.value = _uiState.value.copy(
            isConfirmeSenhaError = isConfirmeSenhaError
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

    fun setEtapa(etapa: EtapaCriarContaEnum) {
        _uiState.value = _uiState.value.copy(
            etapa = etapa
        )
    }

    fun setFocarCodigo(isFocarCodigo: Boolean) {
        _uiState.value = _uiState.value.copy(
            isFocarCodigo = isFocarCodigo
        )
    }

    fun limparCodigo() {
        val codigoLimpo = MutableList(AppConfig.TOTAL_DIGITOS_CODIGO_RECUPERACAO) { "" }
        setCodigo(codigoLimpo)
        setFocarCodigo(true)
    }

    fun setGoogleButtonEnabled(isGoogleButtonEnabled: Boolean) {
        _uiState.value = _uiState.value.copy(isGoogleButtonEnabled = isGoogleButtonEnabled)
    }
}