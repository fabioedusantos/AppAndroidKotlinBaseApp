package br.com.fbsantos.baseapp.domain.usecase.historicoatividades

import br.com.fbsantos.baseapp.data.database.HistoricoAtividadeEntity
import br.com.fbsantos.baseapp.domain.repository.HistoricoAtividadesRepository

class BuscarUltimasAtividadesUseCase(
    private val repository: HistoricoAtividadesRepository
) {
    suspend fun execute(limit: Int = 3): List<HistoricoAtividadeEntity> {
        return repository.list(limit)
    }
}