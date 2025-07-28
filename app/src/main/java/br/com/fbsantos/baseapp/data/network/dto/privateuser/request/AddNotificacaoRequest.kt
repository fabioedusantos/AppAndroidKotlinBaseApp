package br.com.fbsantos.baseapp.data.network.dto.privateuser.request

data class AddNotificacaoRequest(
    val channelId: String,
    val title: String,
    val body: String,
    val link: String
)
