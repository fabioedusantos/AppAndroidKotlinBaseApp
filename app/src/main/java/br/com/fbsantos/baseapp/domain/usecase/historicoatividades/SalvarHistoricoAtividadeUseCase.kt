package br.com.fbsantos.baseapp.domain.usecase.historicoatividades

import br.com.fbsantos.baseapp.domain.repository.HistoricoAtividadesRepository

class SalvarHistoricoAtividadeUseCase (
    private val repository: HistoricoAtividadesRepository
) {
    suspend fun execute(rota: String?, descricao: String, icone: String) {
        repository.save(rota, descricao, icone)
    }
}