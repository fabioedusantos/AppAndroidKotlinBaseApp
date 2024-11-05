package br.com.fbsantos.baseapp.data.database

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [
        ConfiguracaoEntity::class,
        HistoricoAtividadeEntity::class
    ],
    version = 2,
    exportSchema = true
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun configuracoesDao(): ConfiguracoesDao
    abstract fun historicoAtividadesDao(): HistoricoAtividadesDao
}