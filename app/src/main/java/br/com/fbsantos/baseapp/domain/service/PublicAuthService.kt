package br.com.fbsantos.baseapp.domain.service

import br.com.fbsantos.baseapp.data.network.api.PublicAuthApiService
import br.com.fbsantos.baseapp.data.network.dto.publicauth.request.ConfirmEmailRequest
import br.com.fbsantos.baseapp.data.network.dto.publicauth.request.ForgotPasswordRequest
import br.com.fbsantos.baseapp.data.network.dto.publicauth.request.LoginGoogleRequest
import br.com.fbsantos.baseapp.data.network.dto.publicauth.request.LoginRequest
import br.com.fbsantos.baseapp.data.network.dto.publicauth.request.ResetPasswordRequest
import br.com.fbsantos.baseapp.data.network.dto.publicauth.request.SignupGoogleRequest
import br.com.fbsantos.baseapp.data.network.dto.publicauth.request.SignupRequest
import br.com.fbsantos.baseapp.domain.exception.ApiException
import br.com.fbsantos.baseapp.domain.usecase.configuracoes.TokenManagerUseCase
import br.com.fbsantos.baseapp.util.helpers.callApi
import br.com.fbsantos.baseapp.util.helpers.callApiNoResponse

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

    suspend fun confirmEmail(body: ConfirmEmailRequest): Unit = callApiNoResponse(
        request = {
            publicAuthApiService.confirmEmail(body)
        },
        onError = { message ->
            throw ApiException(message)
        }
    )

    suspend fun resendConfirmEmail(body: ForgotPasswordRequest): Unit = callApi(
        request = {
            publicAuthApiService.resendConfirmEmail(body)
        },
        onError = { message ->
            throw ApiException(message)
        }
    )

    suspend fun login(body: LoginRequest): Unit = callApi (
        request = {
            tokenManager.limparTokens()
            publicAuthApiService.login(body)
        },
        onSuccess = { data, message ->
            val token = data?.token
            val refreshToken = data?.refreshToken
            if (!token.isNullOrEmpty() && !refreshToken.isNullOrEmpty()) {
                tokenManager.salvarTokens(token, refreshToken)
            } else{
                throw ApiException("Token inexistente. Tente novamente.")
            }
        },
        onError = { message ->
            throw ApiException(message)
        }
    )

    suspend fun loginGoogle(body: LoginGoogleRequest): Unit = callApi (
        request = {
            tokenManager.limparTokens()
            publicAuthApiService.loginGoogle(body)
        },
        onSuccess = { data, message ->
            val token = data?.token
            val refreshToken = data?.refreshToken
            if (!token.isNullOrEmpty() && !refreshToken.isNullOrEmpty()) {
                tokenManager.salvarTokens(token, refreshToken)
            }
        },
        onError = { message ->
            throw ApiException(message)
        }
    )

    suspend fun forgotPassword(body: ForgotPasswordRequest): Unit = callApi(
        request = {
            publicAuthApiService.forgotPassword(body)
        },
        onError = { message ->
            throw ApiException(message)
        }
    )

    suspend fun checkResetPassword(body: ConfirmEmailRequest): Unit = callApi(
        request = {
            publicAuthApiService.checkResetCode(body)
        },
        onError = { message ->
            throw ApiException(message)
        }
    )

    suspend fun resetPassword(body: ResetPasswordRequest): Unit = callApiNoResponse(
        request = {
            publicAuthApiService.resetPassword(body)
        },
        onError = { message ->
            throw ApiException(message)
        }
    )

    suspend fun logout() {
        tokenManager.limparTokens()
    }
}