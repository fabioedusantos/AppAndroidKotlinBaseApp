package br.com.fbsantos.baseapp.data.network.dto.publicauth.request

data class SignupRequest(
    val name: String,
    val lastname: String,
    val email: String,
    val password: String,
    val isTerms: Boolean,
    val isPolicy: Boolean,
    val recaptchaToken: String,
    val recaptchaSiteKey: String
)