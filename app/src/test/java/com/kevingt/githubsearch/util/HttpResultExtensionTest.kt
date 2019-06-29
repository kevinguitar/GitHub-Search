package com.kevingt.githubsearch.util

import com.kevingt.githubsearch.model.HttpResult
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.runBlocking
import okhttp3.MediaType
import okhttp3.ResponseBody
import org.junit.Test
import retrofit2.Response

class HttpResultExtensionTest {

    @Test
    fun convertSuccessResponseToHttpResult() {
        val response = CompletableDeferred(Response.success(Any()))
        var convertResult: HttpResult<Any>? = null
        runBlocking {
            convertResult = response.convertToHttpResult()
        }
        assert(convertResult is HttpResult.Success)
    }

    @Test
    fun convertApiErrorResponseToHttpResult() {
        val response = CompletableDeferred(
            Response.error<Any>(
                500, ResponseBody.create(MediaType.parse(""), "")
            )
        )
        var convertResult: HttpResult<Any>? = null
        runBlocking {
            convertResult = response.convertToHttpResult()
        }
        assert(convertResult is HttpResult.ApiError)
    }
}