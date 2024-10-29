package br.com.fbsantos.baseapp.data.network.api

import br.com.fbsantos.baseapp.data.network.dto.ApiResponseDefault
import br.com.fbsantos.baseapp.data.network.dto.publicauth.request.ConfirmEmailRequest
import br.com.fbsantos.baseapp.data.network.dto.publicauth.request.ForgotPasswordRequest
import br.com.fbsantos.baseapp.data.network.dto.publicauth.request.LoginGoogleRequest
import br.com.fbsantos.baseapp.data.network.dto.publicauth.request.LoginRequest
import br.com.fbsantos.baseapp.data.network.dto.publicauth.request.SignupGoogleRequest
import br.com.fbsantos.baseapp.data.network.dto.publicauth.request.SignupRequest
import br.com.fbsantos.baseapp.data.network.dto.publicauth.response.ConfirmEmailResponse
import br.com.fbsantos.baseapp.data.network.dto.publicauth.response.TokenResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface PublicAuthApiService {
    @POST("users/signup")
    suspend fun signup(@Body body: SignupRequest): Response<ApiResponseDefault<Unit>>

    @POST("users/google/signup")
    suspend fun signUpGoogle(@Body body: SignupGoogleRequest): Response<ApiResponseDefault<TokenResponse>>

    @POST("users/confirm_email")
    suspend fun confirmEmail(@Body body: ConfirmEmailRequest): Response<Unit>

    @POST("users/resend_confirm_email")
    suspend fun resendConfirmEmail(@Body body: ForgotPasswordRequest): Response<ApiResponseDefault<ConfirmEmailResponse>>

    @POST("users/login")
    suspend fun login(@Body body: LoginRequest): Response<ApiResponseDefault<TokenResponse>>

    @POST("users/google/login")
    suspend fun loginGoogle(@Body body: LoginGoogleRequest): Response<ApiResponseDefault<TokenResponse>>

}