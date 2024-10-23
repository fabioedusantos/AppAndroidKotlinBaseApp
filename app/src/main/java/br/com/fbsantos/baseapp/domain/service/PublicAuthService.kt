package br.com.fbsantos.baseapp.domain.service

import br.com.fbsantos.baseapp.data.network.api.PublicAuthApiService
import br.com.fbsantos.baseapp.domain.usecase.configuracoes.TokenManagerUseCase

class PublicAuthService(
    private val publicAuthApiService: PublicAuthApiService,
    private val tokenManager: TokenManagerUseCase
) {


    suspend fun logout() {
        tokenManager.limparTokens()
    }
}