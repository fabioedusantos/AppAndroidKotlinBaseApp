package br.com.fbsantos.baseapp.data.network.dto.publicauth.request

data class ConfirmEmailRequest(
    val email: String,
    val code: String,
    val recaptchaToken: String,
    val recaptchaSiteKey: String
)
