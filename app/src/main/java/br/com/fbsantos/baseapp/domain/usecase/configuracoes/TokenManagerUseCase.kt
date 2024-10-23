package br.com.fbsantos.baseapp.domain.usecase.configuracoes

import br.com.fbsantos.baseapp.domain.repository.ConfiguracoesRepository
import kotlinx.coroutines.flow.firstOrNull

class TokenManagerUseCase(private val repository: ConfiguracoesRepository) {

    companion object {
        private const val KEY_AUTH_TOKEN = "authToken"
        private const val KEY_REFRESH_TOKEN = "refreshToken"
    }

    suspend fun salvarTokens(authToken: String, refreshToken: String) {
        repository.salvar(KEY_AUTH_TOKEN, authToken)
        repository.salvar(KEY_REFRESH_TOKEN, refreshToken)
    }

    suspend fun getAuthToken(): String? =
        repository.getString(KEY_AUTH_TOKEN).firstOrNull()

    suspend fun getRefreshToken(): String? =
        repository.getString(KEY_REFRESH_TOKEN).firstOrNull()

    suspend fun limparTokens() {
        repository.salvar(KEY_AUTH_TOKEN, "")
        repository.salvar(KEY_REFRESH_TOKEN, "")
    }
}