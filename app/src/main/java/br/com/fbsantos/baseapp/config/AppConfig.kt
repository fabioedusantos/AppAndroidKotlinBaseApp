package br.com.fbsantos.baseapp.config

object AppConfig {
    var appName: String = ""
    const val TAMANHO_MINIMO_SENHA = 8
    const val TOTAL_DIGITOS_CODIGO_RECUPERACAO = 6
    const val TEMPO_ESPERA_REENVIO_EMAIL_SEGUNDOS = 30
    const val IS_DEBUG = true
    const val REQUEST_TIMEOUT_SECONDS = 60L
}