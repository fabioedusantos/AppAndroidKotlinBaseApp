package br.com.fbsantos.baseapp.data.network.dto.publicauth.request

data class ForgotPasswordRequest(
    val email: String,
    val recaptchaToken: String,
    val recaptchaSiteKey: String
)
