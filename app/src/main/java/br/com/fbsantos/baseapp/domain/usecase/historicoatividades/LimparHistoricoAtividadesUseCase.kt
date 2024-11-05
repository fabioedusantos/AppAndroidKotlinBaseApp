package br.com.fbsantos.baseapp.domain.usecase.historicoatividades

import br.com.fbsantos.baseapp.domain.repository.HistoricoAtividadesRepository

class LimparHistoricoAtividadesUseCase(
    private val repository: HistoricoAtividadesRepository
) {
    suspend fun execute() {
        repository.clearAll()
    }
}