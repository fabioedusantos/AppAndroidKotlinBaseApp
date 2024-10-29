package br.com.fbsantos.baseapp.data.network.dto.publicauth.request

data class LoginGoogleRequest(
    val idTokenFirebase: String,
    val recaptchaToken: String,
    val recaptchaSiteKey: String
)