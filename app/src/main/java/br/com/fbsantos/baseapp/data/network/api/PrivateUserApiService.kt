package br.com.fbsantos.baseapp.data.network.api

import retrofit2.Response
import retrofit2.http.GET

interface PrivateUserApiService {
    @GET("users/is_logged_in")
    suspend fun isLoggedIn(): Response<Unit>
}