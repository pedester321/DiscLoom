package com.loginapp.data.repository

import com.loginapp.data.model.ApiResult
import com.loginapp.data.remote.LoginApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import org.json.JSONException
import org.json.JSONObject
import retrofit2.HttpException
import java.io.IOException

class LoginRepository(
    private val loginApi: LoginApi,
){
    suspend fun login(email: String, password: String): Flow<ApiResult<String>> {
        return flow {
            try {
                // Corpo da requisição
                val body = mapOf("email" to email, "password" to password)

                // Fazendo a chamada da API (assumindo que loginApi.postRequest já é uma função `suspend`)
                val response = loginApi.login(body) // chamada suspensa

                // Verificando a resposta
                if (response.isSuccessful && response.body() != null) {
                    val responseBody = response.body()?.string()

                    // Tentar interpretar o corpo como JSON com token
                    try {
                        val jsonObject = responseBody?.let { JSONObject(it) }
                        val token = jsonObject?.getString("token")

                        if (token != null) {
                            emit(ApiResult.Success(token)) // Emite o token em caso de sucesso
                        } else {
                            emit(ApiResult.Error("Token não encontrado na resposta."))
                        }

                    } catch (e: JSONException) {
                        // Caso ocorra um erro ao interpretar o JSON
                        emit(ApiResult.Error(responseBody.toString()))
                    }
                } else {
                    // Resposta não-sucedida
                    emit(ApiResult.Error("Erro na resposta: ${response.errorBody()?.string()}"))
                }

            } catch (e: HttpException) {
                // Erro de HTTP (ex: código de status diferente de 2xx)
                emit(ApiResult.Error("Erro HTTP: ${e.message()}"))
            } catch (e: IOException) {
                // Erro de rede
                emit(ApiResult.Error("Erro de rede: ${e.message}"))
            }
        }
    }
    suspend fun signUp(
        name: String,
        email: String,
        password: String,
        birthDate: String
    ): Flow<ApiResult<String>> {
        return flow {
            try {
                // Corpo da requisição
                val body = mapOf(
                    "name" to name,
                    "email" to email,
                    "password" to password,
                    "birthdate" to birthDate
                )

                // Fazendo a chamada da API (assumindo que loginApi.postRequest já é uma função `suspend`)
                val response = loginApi.signUp(body) // chamada suspensa

                // Verificando a resposta
                if (response.isSuccessful && response.body() != null) {
                    emit(ApiResult.Success(null))

                } else {
                    // Resposta não-sucedida
                    emit(ApiResult.Error("Erro na resposta: ${response.errorBody()?.string()}"))
                }

            } catch (e: HttpException) {
                // Erro de HTTP (ex: código de status diferente de 2xx)
                emit(ApiResult.Error("Erro HTTP: ${e.message()}"))
            } catch (e: IOException) {
                // Erro de rede
                emit(ApiResult.Error("Erro de rede: ${e.message}"))
            }
        }
    }
}