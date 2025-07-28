package br.com.fbsantos.ui.main.perfil

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.fbsantos.baseapp.config.navigation.isValidRoute
import br.com.fbsantos.baseapp.data.network.dto.privateuser.request.AddNotificacaoRequest
import br.com.fbsantos.baseapp.domain.service.PrivateUserService
import br.com.fbsantos.baseapp.ui.screen.app.notificacoes.NotificacoesUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class NotificacoesViewModel(
    private val privateUserService: PrivateUserService
) : ViewModel() {

    private val _uiState = MutableStateFlow(NotificacoesUiState())
    val uiState: StateFlow<NotificacoesUiState> = _uiState


    fun onSalvar(onSuccess: () -> Unit) {
        setFormEnabled(false)
        resetAllErrors()

        var isValido = true

        if (getChannelId().isEmpty()) {
            setChannelIdErrorText("Selecione um canal.")
            isValido = false
        }
        if (getTitle().trim().length < 2) {
            setTitleErrorText("Digite ao menos 2 letras para o titulo.")
            isValido = false
        }
        if (getBody().trim().length > 0 && getBody().trim().length < 2) {
            setBodyErrorText("Digite ao menos 2 letras para o corpo.")
            isValido = false
        }
        if (getLink().isNotEmpty() && !isValidRoute(getLink())) {
            setLinkErrorText("O link deve ser uma rota válida da aplicação.")
            isValido = false
        }

        if (!isValido) {
            setFormEnabled(true)
            return
        }

        viewModelScope.launch {
            try {
                setFormEnabled(false)
                privateUserService.addNotificacao(
                    AddNotificacaoRequest(
                        channelId = getChannelId(),
                        title = getTitle(),
                        body = getBody(),
                        link = getLink()
                    )
                )

                setError(null)
                onSuccess()
            } catch (e: Exception) {
                setError(e.message)
            }
            setFormEnabled(true)
        }
    }


    fun onChannelIdChange(channelId: String) {
        setChannelId(channelId)
        setChannelIdErrorText(null)
    }

    fun onTitleChange(title: String) {
        setTitle(title)
        setTitleErrorText(null)
    }

    fun onBodyChange(body: String) {
        setBody(body)
        setBodyErrorText(null)
    }

    fun onLinkChange(link: String) {
        setLink(link)
        setLinkErrorText(null)
    }

    fun resetAllErrors() {
        setChannelIdErrorText(null)
        setTitleErrorText(null)
        setBodyErrorText(null)
        setLinkErrorText(null)
        setError(null)
    }


    fun getChannelId(): String {
        return _uiState.value.channelId
    }

    fun setChannelId(channelId: String) {
        _uiState.value = _uiState.value.copy(channelId = channelId)
    }

    fun setChannelIdErrorText(channelIdErrorText: String?) {
        _uiState.value = _uiState.value.copy(
            isChannelIdError = !channelIdErrorText.isNullOrEmpty(),
            channelIdErrorText = channelIdErrorText.orEmpty()
        )
    }

    fun getTitle(): String {
        return _uiState.value.title
    }

    fun setTitle(title: String) {
        _uiState.value = _uiState.value.copy(title = title)
    }

    fun setTitleErrorText(titleErrorText: String?) {
        _uiState.value = _uiState.value.copy(
            isTitleError = !titleErrorText.isNullOrEmpty(),
            titleErrorText = titleErrorText.orEmpty()
        )
    }

    fun getBody(): String {
        return _uiState.value.body
    }

    fun setBody(body: String) {
        _uiState.value = _uiState.value.copy(body = body)
    }

    fun setBodyErrorText(bodyErrorText: String?) {
        _uiState.value = _uiState.value.copy(
            isBodyError = !bodyErrorText.isNullOrEmpty(),
            bodyErrorText = bodyErrorText.orEmpty()
        )
    }

    fun getLink(): String {
        return _uiState.value.link
    }

    fun setLink(link: String) {
        _uiState.value = _uiState.value.copy(link = link)
    }

    fun setLinkErrorText(linkErrorText: String?) {
        _uiState.value = _uiState.value.copy(
            isLinkError = !linkErrorText.isNullOrEmpty(),
            linkErrorText = linkErrorText.orEmpty()
        )
    }


    fun setFormEnabled(isFormEnabled: Boolean) {
        _uiState.value = _uiState.value.copy(isFormEnabled = isFormEnabled)
    }

    fun setError(error: String?) {
        _uiState.value = _uiState.value.copy(error = error)
    }
}