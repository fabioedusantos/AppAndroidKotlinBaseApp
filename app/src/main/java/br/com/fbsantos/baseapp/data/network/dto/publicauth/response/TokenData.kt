package br.com.fbsantos.baseapp.data.network.dto.publicauth.response

data class TokenData(
    val token: String,
    val refreshToken: String
)