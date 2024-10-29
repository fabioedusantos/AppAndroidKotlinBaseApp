package br.com.fbsantos.baseapp.data.database

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [
        ConfiguracaoEntity::class
    ],
    version = 1
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun configuracoesDao(): ConfiguracoesDao
}