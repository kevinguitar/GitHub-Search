package com.kevingt.githubsearch.model

sealed class HttpResult<out T : Any> {
    class Success<T : Any>(val data: T) : HttpResult<T>()
    class ApiError(val message: String) : HttpResult<Nothing>()
    class NetworkError(val exception: Throwable) : HttpResult<Nothing>()
}