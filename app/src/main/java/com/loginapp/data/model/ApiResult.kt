package com.loginapp.data.model

sealed class ApiResult<T>(
    val data: T? = null,
    val message: String? = null
) {
    class Success<T>(data: T?) : ApiResult<T>(data)
    class Error<T>(message: String, data: T? = null) : ApiResult<T>(data, message)
}