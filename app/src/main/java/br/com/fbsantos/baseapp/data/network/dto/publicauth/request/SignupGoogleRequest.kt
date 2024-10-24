package br.com.fbsantos.baseapp.data.network.dto.publicauth.request

data class SignupGoogleRequest(
    val idTokenFirebase: String,
    val name: String,
    val lastname: String,
    val isTerms: Boolean,
    val isPolicy: Boolean,
    val recaptchaToken: String,
    val recaptchaSiteKey: String
)