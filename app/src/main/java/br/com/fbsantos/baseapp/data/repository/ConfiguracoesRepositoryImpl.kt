package br.com.fbsantos.baseapp.data.repository

import br.com.fbsantos.baseapp.data.database.ConfiguracaoEntity
import br.com.fbsantos.baseapp.data.database.ConfiguracoesDao
import br.com.fbsantos.baseapp.domain.repository.ConfiguracoesRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class ConfiguracoesRepositoryImpl(
    private val dao: ConfiguracoesDao
) : ConfiguracoesRepository {

    override fun getBoolean(key: String): Flow<Boolean?> = flow {
        emit(dao.get(key)?.value?.toBooleanStrictOrNull())
    }

    override fun getString(key: String): Flow<String?> = flow {
        emit(dao.get(key)?.value)
    }

    override fun getInt(key: String): Flow<Int?> = flow {
        emit(dao.get(key)?.value?.toIntOrNull())
    }

    override fun getDouble(key: String): Flow<Double?> = flow {
        emit(dao.get(key)?.value?.toDoubleOrNull())
    }

    override fun getFloat(key: String): Flow<Float?> = flow {
        emit(dao.get(key)?.value?.toFloatOrNull())
    }

    override fun getLong(key: String): Flow<Long?> = flow {
        emit(dao.get(key)?.value?.toLongOrNull())
    }

    override suspend fun salvar(key: String, value: String) {
        dao.save(ConfiguracaoEntity(key, value))
    }

    override suspend fun salvar(key: String, value: Boolean) {
        dao.save(ConfiguracaoEntity(key, value.toString()))
    }

    override suspend fun salvar(key: String, value: Int) {
        dao.save(ConfiguracaoEntity(key, value.toString()))
    }

    override suspend fun salvar(key: String, value: Double) {
        dao.save(ConfiguracaoEntity(key, value.toString()))
    }

    override suspend fun salvar(key: String, value: Float) {
        dao.save(ConfiguracaoEntity(key, value.toString()))
    }

    override suspend fun salvar(key: String, value: Long) {
        dao.save(ConfiguracaoEntity(key, value.toString()))
    }

    override suspend fun clearAll() {
        dao.clearAll()
    }
}