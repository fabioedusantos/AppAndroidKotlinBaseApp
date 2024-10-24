package br.com.fbsantos.baseapp.util

import android.util.Log
import br.com.fbsantos.baseapp.config.AppConfig
import br.com.fbsantos.baseapp.data.network.dto.ApiResponseDefault
import br.com.fbsantos.baseapp.domain.exception.ApiException
import com.google.gson.Gson
import retrofit2.HttpException
import retrofit2.Response
import java.io.IOException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

/**
 * Executa uma chamada de rede Retrofit com tratamento padronizado de sucesso e erro,
 * utilizando como base a estrutura de resposta [ApiResponse<T>].
 *
 * @param request função que executa a requisição e retorna Response<ApiResponse<T>>
 * @param onSuccess callback executado se a requisição for bem-sucedida (status HTTP 2xx e status == "success")
 * @param onError callback executado em caso de erro de negócio ou falha de requisição
 *
 * @return valor retornado pelo onSuccess ou onError
 */
inline fun <T, R> callApi(
    request: () -> Response<ApiResponseDefault<T>>,
    onSuccess: (data: T?, message: String) -> R = { _, _ -> Unit as R },
    onError: (message: String) -> R
): R {
    return runSafeCall {
        val response = request()
        val errorBody = response.errorBody()?.string()
        DebugHttpHelper.log(response, errorBody)

        when {
            response.isSuccessful -> {
                val body = response.body()
                if (body?.status == "success") {
                    onSuccess(body.data, body.message)
                } else {
                    onError(body?.message ?: "Erro desconhecido.")
                }
            }

            else -> {
                onError(apiErrorMessage(response, errorBody))
            }
        }
    }
}

/**
 * Executa uma chamada de rede Retrofit sem resposta esperada no corpo (ex: status 204),
 * com tratamento padronizado de erro e logging.
 *
 * @param request função que executa a requisição Retrofit
 * @param onSuccess callback em caso de sucesso (status HTTP 2xx)
 * @param onError callback em caso de erro
 *
 * @return valor retornado pelo onSuccess ou onError
 */
inline fun <R> callApiNoResponse(
    request: () -> Response<*>,
    onSuccess: (message: String) -> R = { _ -> Unit as R },
    onError: (message: String) -> R
): R {
    return runSafeCall {
        val response = request()
        val errorBody = response.errorBody()?.string()
        DebugHttpHelper.log(response, errorBody)

        if (response.isSuccessful) {
            onSuccess("Sucesso.")
        } else {
            onError(apiErrorMessage(response, errorBody))
        }
    }
}

/**
 * Extrai a mensagem de erro de um corpo de resposta (errorBody) baseado no formato padrão [ApiResponse].
 *
 * @param response objeto de resposta HTTP
 * @param errorBody corpo de erro da resposta como string (pré-extraído)
 * @return mensagem de erro extraída da API ou uma mensagem genérica com base no status HTTP
 */
fun apiErrorMessage(response: Response<*>, errorBody: String?): String {
    val apiError = Gson().fromJson(errorBody, ApiResponseDefault::class.java)

    if (apiError?.status == "error") {
        return apiError.message
    }
    return "Erro no servidor (código ${response.code()})."
}

/**
 * Bloco de execução protegido para chamadas de rede com tratamento padrão de exceções.
 *
 * Captura exceções como IOException, HttpException, ApiException e outras,
 * lançando mensagens apropriadas ou propagando erros de domínio.
 *
 * @param block bloco a ser executado de forma segura
 * @return resultado do bloco ou exceção controlada
 */
inline fun <R> runSafeCall(block: () -> R): R {
    return try {
        block()
    } catch (e: ApiException) {
        throw e
    } catch (e: IOException) {
        if (AppConfig.IS_DEBUG) Log.i(DebugHttpHelper.DEBUG_TAG_REQUEST, e.stackTraceToString())
        if (e is UnknownHostException || e is SocketTimeoutException) {
            throw Exception("Servidor indisponível no momento. Tente novamente mais tarde.")
        } else {
            throw Exception("Falha de conexão. Verifique sua internet.")
        }
    } catch (e: HttpException) {
        if (AppConfig.IS_DEBUG) Log.i(DebugHttpHelper.DEBUG_TAG_REQUEST, e.stackTraceToString())
        throw Exception("Servidor indisponível no momento. Tente mais tarde.")
    } catch (e: Exception) {
        if (AppConfig.IS_DEBUG) Log.i(DebugHttpHelper.DEBUG_TAG_REQUEST, e.stackTraceToString())
        throw Exception("Erro inesperado. Tente novamente.")
    }
}