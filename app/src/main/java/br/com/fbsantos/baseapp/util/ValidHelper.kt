package br.com.fbsantos.baseapp.util

import br.com.fbsantos.baseapp.config.AppConfig

object ValidHelper {
    /**
     * Verifica se o e-mail informado está em um formato válido.
     *
     * O e-mail deve começar com uma letra, conter um '@', seguido por domínio e extensão.
     *
     * @param email E-mail a ser validado.
     * @return `true` se o e-mail for válido, `false` caso contrário.
     */
    fun isEmail(email: String): Boolean {
        return email.matches(Regex("^[A-Za-z](.*)([@])(.+)(\\.)(.+)"))
    }

    /**
     * Valida se a senha atende aos critérios de segurança definidos:
     * - Pelo menos uma letra maiúscula.
     * - Pelo menos um número.
     * - Pelo menos um caractere especial.
     * - Tamanho mínimo definido em [AppConfig.TAMANHO_MINIMO_SENHA].
     *
     * @param senha Senha a ser validada.
     * @return `true` se a senha atender a todos os critérios, `false` caso contrário.
     */
    fun isSenha(senha: String): Boolean {
        return senha.matches(Regex("^(?=.*[A-Z])(?=.*\\d)(?=.*[@#\$%^&+=!]).{${AppConfig.TAMANHO_MINIMO_SENHA},}\$"))
    }

    /**
     * Verifica se o tamanho da senha é maior ou igual que o mínimo definido em [AppConfig.TAMANHO_MINIMO_SENHA].
     *
     * @param senha Senha a ser verificada.
     * @return `true` se o comprimento da senha for maior ou igual que o mínimo, `false` caso contrário.
     */
    fun isTamanhoSenha(senha: String): Boolean {
        return senha.length >= AppConfig.TAMANHO_MINIMO_SENHA
    }
}