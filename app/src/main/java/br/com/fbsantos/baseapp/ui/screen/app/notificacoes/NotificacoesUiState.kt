package br.com.fbsantos.baseapp.ui.screen.app.notificacoes

data class NotificacoesUiState(
    val error: String? = null,
    val isFormEnabled: Boolean = true,

    val channelId: String = "gerais",
    val isChannelIdError: Boolean = false,
    val channelIdErrorText: String = "",

    val title: String = "",
    val isTitleError: Boolean = false,
    val titleErrorText: String = "",

    val body: String = "",
    val isBodyError: Boolean = false,
    val bodyErrorText: String = "",

    val link: String = "",
    val isLinkError: Boolean = false,
    val linkErrorText: String = "",
)
