package br.com.fbsantos.baseapp.domain.exception

/**
 * Exceção base para mensagens de erro controladas retornadas pela API.
 *
 * Use esta exceção quando a resposta da API contiver uma mensagem de erro
 * que deve ser exibida diretamente para o usuário (ex: 400, 401, 422).
 *
 * @param message mensagem descritiva da falha
 */
open class ApiException(message: String) : Exception(message)