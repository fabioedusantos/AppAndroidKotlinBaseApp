package br.com.fbsantos.baseapp.domain.service

import br.com.fbsantos.baseapp.data.network.api.PrivateUserApiService
import br.com.fbsantos.baseapp.util.callApiNoResponse

class PrivateUserService(
    private val privateUserApiService: PrivateUserApiService
) {
    suspend fun isLoggedIn(): Boolean = callApiNoResponse(
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
}