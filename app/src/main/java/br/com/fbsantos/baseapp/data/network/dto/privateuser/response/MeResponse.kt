package br.com.fbsantos.baseapp.data.network.dto.privateuser.response

data class MeResponse(
    val nome: String,
    val sobrenome: String,
    val email: String,
    val photoBlob: String?,
    val ultimoAcesso: String?,
    val criadoEm: String,
    val alteradoEm: String?,
    val isContaGoogle: Boolean,
)