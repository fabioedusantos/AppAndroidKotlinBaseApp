package br.com.fbsantos.baseapp.data.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface HistoricoAtividadesDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun save(atividade: HistoricoAtividadeEntity)

    @Query("SELECT * FROM historico_atividades ORDER BY timestamp DESC LIMIT :limit")
    suspend fun list(limit: Int = 3): List<HistoricoAtividadeEntity>

    @Query("DELETE FROM historico_atividades WHERE id NOT IN (SELECT id FROM historico_atividades ORDER BY timestamp DESC LIMIT :limit)")
    suspend fun keepLatest(limit: Int = 10)

    @Query("DELETE FROM historico_atividades")
    suspend fun clearAll()
}