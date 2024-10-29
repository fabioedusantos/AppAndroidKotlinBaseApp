package br.com.fbsantos.baseapp.data.network.dto.publicauth.request

data class ResetPasswordRequest(
    val email: String,
    val code: String,
    val password: String,
    val recaptchaToken: String,
    val recaptchaSiteKey: String
)
