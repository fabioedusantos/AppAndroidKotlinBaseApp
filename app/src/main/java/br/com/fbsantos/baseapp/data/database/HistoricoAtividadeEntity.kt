package br.com.fbsantos.baseapp.data.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "historico_atividades")
data class HistoricoAtividadeEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val rota: String?,
    val descricao: String,
    val icone: String, // String enum que é usada por IconHelper
    val timestamp: Long = System.currentTimeMillis()
)