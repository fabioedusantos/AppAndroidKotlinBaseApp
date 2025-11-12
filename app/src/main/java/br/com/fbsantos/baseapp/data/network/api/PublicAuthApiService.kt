package br.com.fbsantos.baseapp.data.network.api

import br.com.fbsantos.baseapp.data.network.dto.ApiResponseDefault
import br.com.fbsantos.baseapp.data.network.dto.publicauth.request.ConfirmEmailRequest
import br.com.fbsantos.baseapp.data.network.dto.publicauth.request.ForgotPasswordRequest
import br.com.fbsantos.baseapp.data.network.dto.publicauth.request.LoginGoogleRequest
import br.com.fbsantos.baseapp.data.network.dto.publicauth.request.LoginRequest
import br.com.fbsantos.baseapp.data.network.dto.publicauth.request.ResetPasswordRequest
import br.com.fbsantos.baseapp.data.network.dto.publicauth.request.SignupGoogleRequest
import br.com.fbsantos.baseapp.data.network.dto.publicauth.request.SignupRequest
import br.com.fbsantos.baseapp.data.network.dto.publicauth.response.ConfirmEmailResponse
import br.com.fbsantos.baseapp.data.network.dto.publicauth.response.TokenResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface PublicAuthApiService {
    @POST("auth/signup")
    suspend fun signup(@Body body: SignupRequest): Response<ApiResponseDefault<Unit>>

    @POST("auth/google/signup")
    suspend fun signUpGoogle(@Body body: SignupGoogleRequest): Response<ApiResponseDefault<TokenResponse>>

    @POST("auth/confirm_email")
    suspend fun confirmEmail(@Body body: ConfirmEmailRequest): Response<Unit>

    @POST("auth/resend_confirm_email")
    suspend fun resendConfirmEmail(@Body body: ForgotPasswordRequest): Response<ApiResponseDefault<ConfirmEmailResponse>>

    @POST("auth/login")
    suspend fun login(@Body body: LoginRequest): Response<ApiResponseDefault<TokenResponse>>

    @POST("auth/google/login")
    suspend fun loginGoogle(@Body body: LoginGoogleRequest): Response<ApiResponseDefault<TokenResponse>>

    @POST("auth/forgot_password")
    suspend fun forgotPassword(@Body body: ForgotPasswordRequest): Response<ApiResponseDefault<ConfirmEmailResponse>>

    @POST("auth/check_reset_code")
    suspend fun checkResetCode(@Body body: ConfirmEmailRequest): Response<ApiResponseDefault<Unit>>

    @POST("auth/reset_password")
    suspend fun resetPassword(@Body body: ResetPasswordRequest): Response<Unit>

}