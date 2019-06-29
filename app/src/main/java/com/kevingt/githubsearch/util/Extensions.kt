package com.kevingt.githubsearch.util

import com.kevingt.githubsearch.model.HttpResult
import kotlinx.coroutines.Deferred
import retrofit2.Response

/**
 * @return  HttpResult: convert from Response, to be more easy to handle
 */

suspend fun <T> Deferred<Response<T>>.convertToHttpResult(): HttpResult<Response<T>> {
    return try {
        val result = await()
        if (result.isSuccessful) {
            HttpResult.Success(result)
        } else {
            HttpResult.ApiError(result.message())
        }
    } catch (e: Throwable) {
        HttpResult.NetworkError(e)
    }
}