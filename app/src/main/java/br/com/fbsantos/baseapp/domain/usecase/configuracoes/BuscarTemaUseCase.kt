package br.com.fbsantos.baseapp.domain.usecase.configuracoes

import br.com.fbsantos.baseapp.domain.repository.ConfiguracoesRepository

class BuscarTemaUseCase(
    private val repository: ConfiguracoesRepository
) {
    fun execute() = repository.getBoolean("isDarkTheme")
}