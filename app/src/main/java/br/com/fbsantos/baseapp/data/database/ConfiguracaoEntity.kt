package br.com.fbsantos.baseapp.data.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "configuracoes")
data class ConfiguracaoEntity(
    @PrimaryKey val key: String,
    val value: String
)