package br.com.fbsantos.baseapp.config

object AppConfig {
    //geral
    var appName: String = ""
    const val APP_DB = "base_app.db"
    const val IS_DEBUG = true

    //webapi
    const val REQUEST_TIMEOUT_SECONDS = 60L
    const val API_BASE_URL = "https://10.1.0.10/"
    const val API_TOKEN_REFRESH_URL = API_BASE_URL + "token/refresh"

    //forms
    const val TAMANHO_MINIMO_SENHA = 8
    const val TOTAL_DIGITOS_CODIGO_RECUPERACAO = 6
    const val TEMPO_ESPERA_REENVIO_EMAIL_SEGUNDOS = 30

    //recaptcha
    const val RECAPTCHA_SITE_KEY = "6Ld85XQrAAAAACMw8m2b885JksCyw5Myb-_I5ZLa"
}