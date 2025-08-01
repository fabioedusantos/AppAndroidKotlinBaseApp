package br.com.fbsantos.baseapp.domain.service

import br.com.fbsantos.baseapp.data.network.api.PrivateUserApiService
import br.com.fbsantos.baseapp.data.network.dto.privateuser.request.AddNotificacaoRequest
import br.com.fbsantos.baseapp.data.network.dto.privateuser.request.SetMeRequest
import br.com.fbsantos.baseapp.data.network.dto.privateuser.response.MeResponse
import br.com.fbsantos.baseapp.domain.exception.ApiException
import br.com.fbsantos.baseapp.util.helpers.callApi
import br.com.fbsantos.baseapp.util.helpers.callApiAuth
import br.com.fbsantos.baseapp.util.helpers.callApiAuthNoResponse
import br.com.fbsantos.baseapp.util.helpers.callApiNoResponse

class PrivateUserService(
    private val privateUserApiService: PrivateUserApiService
) {
    suspend fun isLoggedIn(): Boolean = callApiAuthNoResponse(
        request = {
            privateUserApiService.isLoggedIn()
        },
        onSuccess = { message ->
            true
        },
        onError = { message ->
            false
        }
    )

    suspend fun getMe(): MeResponse = callApiAuth(
        request = {
            privateUserApiService.getMe()
        },
        onSuccess = { data, message ->
            data ?: throw ApiException("Servidor você não pode fazer isto.")
        },
        onError = { message ->
            throw ApiException(message)
        }
    )

    suspend fun setMe(body: SetMeRequest): Unit = callApiAuthNoResponse(
        request = {
            privateUserApiService.setMe(body)
        },
        onError = { message ->
            throw ApiException(message)
        }
    )

    suspend fun addNotificacao(body: AddNotificacaoRequest): Unit = callApiAuth(
        request = {
            privateUserApiService.addNotificacao(body)
        },
        onError = { message ->
            throw ApiException(message)
        }
    )
}