package br.com.fbsantos.ui.main.perfil

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.fbsantos.baseapp.util.Valid
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class PerfilViewModel() : ViewModel() {

    private val _uiState = MutableStateFlow(PerfilUiState())
    val uiState: StateFlow<PerfilUiState> = _uiState

    fun onEditarPerfil(isContaGoogle: Boolean, onSuccess: () -> Unit) {
        setFormEnabled(false)
        resetAllErrors()

        var isValido = true

        if (!isNomeValido()) {
            isValido = false
        }

        if (!isSobrenomeValido()) {
            isValido = false
        }

        if (!isContaGoogle) {
            if (getSenha().length > 0 && !Valid.isSenha(getSenha())) {
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
        }

        if (!isValido) {
            setFormEnabled(true)
            return
        }

        viewModelScope.launch {
            try {
                setFormEnabled(false)
                //todo enviar Me
//                name = getNome(),
//                lastname = getSobrenome(),
//                password = getSenha(),
//                photoBlob = getFoto(),
//                isRemovePhoto = getRemoverFoto()

                setError(null)
                onSuccess()
            } catch (e: Exception) {
                setError(e.message)
            }
            setFormEnabled(true)
        }
    }

    fun onSetFoto(base64: String) {
        setFoto(base64)
        setRemoverFoto(false)
    }

    fun onRemoverFoto() {
        setRemoverFoto(true)
        setFoto("")
    }

    fun getFoto(): String {
        return _uiState.value.foto
    }

    fun setFoto(foto: String) {
        _uiState.value = _uiState.value.copy(foto = foto)
    }

    fun getRemoverFoto(): Boolean {
        return _uiState.value.isRemoverFoto
    }

    fun setRemoverFoto(isRemoverFoto: Boolean) {
        _uiState.value = _uiState.value.copy(isRemoverFoto = isRemoverFoto)
    }

    fun onToggleEditarPerfil() {
        setFormEnabled(false)
        setEditarPerfil(!getEditarPerfil())
        setFormEnabled(true)
    }

    fun getEditarPerfil(): Boolean {
        return _uiState.value.isEditarPerfil
    }

    fun setEditarPerfil(isEditarPerfil: Boolean) {
        _uiState.value = _uiState.value.copy(isEditarPerfil = isEditarPerfil)
    }

    fun onNomeChange(nome: String) {
        setNome(nome)
        setNomeErrorText(null)
    }

    fun onSobreNomeChange(sobreNome: String) {
        setSobrenome(sobreNome)
        setSobrenomeErrorText(null)
    }

    fun onSenhaChange(senha: String) {
        setSenha(senha)
        setSenhaErrorText(null)
    }

    fun onSenhaVisivelToggle() {
        setSenhaVisivel(!getSenhaVisivel())
    }

    fun onConfirmeSenhaChange(confirmeSenha: String) {
        setConfirmeSenha(confirmeSenha)
        setConfirmeSenhaError(false)
    }

    fun onConfirmeSenhaVisivelToggle() {
        setConfirmeSenhaVisivel(!getConfirmeSenhaVisivel())
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

    fun resetAllErrors() {
        setNomeErrorText(null)
        setSobrenomeErrorText(null)
        setSenhaErrorText(null)
        setError(null)
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

    fun getSenhaVisivel(): Boolean {
        return _uiState.value.isSenhaVisivel
    }

    fun setSenhaVisivel(isSenhaVisivel: Boolean) {
        _uiState.value = _uiState.value.copy(isSenhaVisivel = isSenhaVisivel)
    }

    fun setConfirmeSenhaVisivel(isConfirmeSenhaVisivel: Boolean) {
        _uiState.value = _uiState.value.copy(isConfirmeSenhaVisivel = isConfirmeSenhaVisivel)
    }

    fun getConfirmeSenhaVisivel(): Boolean {
        return _uiState.value.isConfirmeSenhaVisivel
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

    fun setFormEnabled(isFormEnabled: Boolean) {
        _uiState.value = _uiState.value.copy(isFormEnabled = isFormEnabled)
    }

    fun setError(error: String?) {
        _uiState.value = _uiState.value.copy(error = error)
    }
}