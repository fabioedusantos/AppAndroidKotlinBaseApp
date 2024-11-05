package br.com.fbsantos.baseapp.data.repository

import br.com.fbsantos.baseapp.data.database.HistoricoAtividadeEntity
import br.com.fbsantos.baseapp.data.database.HistoricoAtividadesDao
import br.com.fbsantos.baseapp.domain.repository.HistoricoAtividadesRepository

class HistoricoAtividadesRepositoryImpl(
    private val dao: HistoricoAtividadesDao
) : HistoricoAtividadesRepository {
    override suspend fun save(rota: String?, descricao: String, icone: String) {
        dao.save(
            HistoricoAtividadeEntity(
            rota = rota,
            descricao = descricao,
            icone = icone
        )
        )
        dao.keepLatest(10) // mantém apenas 10 registros recentes
    }

    override suspend fun list(limit: Int): List<HistoricoAtividadeEntity> {
        return dao.list(limit)
    }

    override suspend fun clearAll() {
        dao.clearAll()
    }
}