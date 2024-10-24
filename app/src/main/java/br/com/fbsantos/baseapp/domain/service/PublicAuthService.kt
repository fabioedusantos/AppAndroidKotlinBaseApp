package br.com.fbsantos.baseapp.domain.service

import br.com.fbsantos.baseapp.data.network.api.PublicAuthApiService
import br.com.fbsantos.baseapp.data.network.dto.publicauth.request.SignupGoogleRequest
import br.com.fbsantos.baseapp.data.network.dto.publicauth.request.SignupRequest
import br.com.fbsantos.baseapp.domain.exception.ApiException
import br.com.fbsantos.baseapp.domain.usecase.configuracoes.TokenManagerUseCase
import br.com.fbsantos.baseapp.util.callApi

class PublicAuthService(
    private val publicAuthApiService: PublicAuthApiService,
    private val tokenManager: TokenManagerUseCase
) {

    suspend fun signup(body: SignupRequest): Unit = callApi(
        request = {
            publicAuthApiService.signup(body)
        },
        onError = { message ->
            throw ApiException(message)
        }
    )

    suspend fun signUpGoogle(body: SignupGoogleRequest): Unit = callApi(
        request = {
            tokenManager.limparTokens()
            publicAuthApiService.signUpGoogle(body)
        },
        onSuccess = { data, message ->
            val token = data?.token
            val refreshToken = data?.refreshToken
            if (!token.isNullOrEmpty() && !refreshToken.isNullOrEmpty()) {
                tokenManager.salvarTokens(token, refreshToken)
            } else {
                throw ApiException("Token inexistente. Tente novamente.")
            }
        },
        onError = { message ->
            throw ApiException(message)
        }
    )

    suspend fun logout() {
        tokenManager.limparTokens()
    }
}