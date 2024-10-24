package br.com.fbsantos.baseapp.data.network.dto

data class ApiResponseDefault<T>(
    val status: String,
    val message: String,
    val data: T?
)