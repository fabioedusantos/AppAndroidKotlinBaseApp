package br.com.fbsantos.baseapp.domain.repository

import br.com.fbsantos.baseapp.data.database.HistoricoAtividadeEntity

interface HistoricoAtividadesRepository {
    suspend fun save(rota: String?, descricao: String, icone: String)
    suspend fun list(limit: Int = 3): List<HistoricoAtividadeEntity>
    suspend fun clearAll()
}