package br.com.fbsantos.baseapp.domain.repository

import kotlinx.coroutines.flow.Flow

interface ConfiguracoesRepository {
    fun getBoolean(key: String): Flow<Boolean?>
    fun getString(key: String): Flow<String?>
    fun getInt(key: String): Flow<Int?>
    fun getDouble(key: String): Flow<Double?>
    fun getFloat(key: String): Flow<Float?>
    fun getLong(key: String): Flow<Long?>

    suspend fun salvar(key: String, value: String)
    suspend fun salvar(key: String, value: Boolean)
    suspend fun salvar(key: String, value: Int)
    suspend fun salvar(key: String, value: Double)
    suspend fun salvar(key: String, value: Float)
    suspend fun salvar(key: String, value: Long)
    suspend fun clearAll()
}