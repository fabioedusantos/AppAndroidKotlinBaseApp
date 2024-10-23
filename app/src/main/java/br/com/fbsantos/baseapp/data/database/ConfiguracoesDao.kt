package br.com.fbsantos.baseapp.data.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface ConfiguracoesDao {

    @Query("SELECT * FROM configuracoes WHERE `key` = :key LIMIT 1")
    suspend fun get(key: String): ConfiguracaoEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun save(configuracao: ConfiguracaoEntity)

    @Query("DELETE FROM configuracoes")
    suspend fun clearAll()
}