package br.com.fbsantos.baseapp.data.network.dto.privateuser.request

data class SetMeRequest(
    val name: String,
    val lastname: String,
    val password: String,
    val photoBlob: String,
    val isRemovePhoto: Boolean
)
