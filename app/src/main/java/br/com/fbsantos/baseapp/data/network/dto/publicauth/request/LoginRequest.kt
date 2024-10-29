package br.com.fbsantos.baseapp.data.network.dto.publicauth.request

data class LoginRequest(
    val email: String,
    val password: String,
    val recaptchaToken: String,
    val recaptchaSiteKey: String
)