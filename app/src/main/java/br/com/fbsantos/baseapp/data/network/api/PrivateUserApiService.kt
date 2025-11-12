package br.com.fbsantos.baseapp.data.network.api

import br.com.fbsantos.baseapp.data.network.dto.ApiResponseDefault
import br.com.fbsantos.baseapp.data.network.dto.privateuser.request.AddNotificacaoRequest
import br.com.fbsantos.baseapp.data.network.dto.privateuser.request.SetMeRequest
import br.com.fbsantos.baseapp.data.network.dto.privateuser.response.MeResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST

interface PrivateUserApiService {
    @GET("auth/is_logged_in")
    suspend fun isLoggedIn(): Response<Unit>

    @GET("users/me")
    suspend fun getMe(): Response<ApiResponseDefault<MeResponse>>

    @PATCH("users/me")
    suspend fun setMe(@Body body: SetMeRequest): Response<Unit>

    @POST("notifications/send_push_to_all")
    suspend fun addNotificacao(@Body body: AddNotificacaoRequest): Response<ApiResponseDefault<Unit>>
}