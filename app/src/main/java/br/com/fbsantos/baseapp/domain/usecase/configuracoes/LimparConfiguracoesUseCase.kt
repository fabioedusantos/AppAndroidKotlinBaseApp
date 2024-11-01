package br.com.fbsantos.baseapp.domain.usecase.configuracoes

import br.com.fbsantos.baseapp.domain.repository.ConfiguracoesRepository

class LimparConfiguracoesUseCase(
    private val repository: ConfiguracoesRepository
) {
    suspend fun execute() {
        repository.clearAll()
    }
}