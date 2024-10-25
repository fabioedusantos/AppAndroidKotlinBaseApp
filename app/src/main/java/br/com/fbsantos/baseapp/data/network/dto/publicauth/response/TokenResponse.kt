package br.com.fbsantos.baseapp.data.network.dto.publicauth.response

data class TokenResponse(
    val token: String,
    val refreshToken: String
)