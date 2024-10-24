package br.com.fbsantos.baseapp.data.network.api

import br.com.fbsantos.baseapp.data.network.dto.ApiResponseDefault
import br.com.fbsantos.baseapp.data.network.dto.publicauth.request.SignupGoogleRequest
import br.com.fbsantos.baseapp.data.network.dto.publicauth.request.SignupRequest
import br.com.fbsantos.baseapp.data.network.dto.publicauth.response.TokenData
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface PublicAuthApiService {
    @POST("users/signup")
    suspend fun signup(@Body body: SignupRequest): Response<ApiResponseDefault<Unit>>

    @POST("users/google/signup")
    suspend fun signUpGoogle(@Body body: SignupGoogleRequest): Response<ApiResponseDefault<TokenData>>

}