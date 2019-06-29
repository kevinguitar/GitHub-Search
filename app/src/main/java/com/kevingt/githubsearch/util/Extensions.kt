package com.kevingt.githubsearch.util

import androidx.lifecycle.MutableLiveData
import com.kevingt.githubsearch.model.HttpResult
import kotlinx.coroutines.Deferred
import retrofit2.Response


/**
 * @param initialValue  Set default value for LiveData
 */
fun <T : Any?> MutableLiveData<T>.default(initialValue: T) = apply { this.value = initialValue }


/**
 * @return  Solving the problem for list modification that won't notify observer
 */
fun <T> MutableLiveData<List<T>>.addAllAndNotifyObserver(items: List<T>) {
    val updatedItems = this.value as MutableList
    updatedItems.addAll(items)
    this.value = updatedItems
}


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