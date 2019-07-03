package com.kevingt.githubsearch.model

import androidx.annotation.VisibleForTesting
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.kevingt.githubsearch.BuildConfig
import com.kevingt.githubsearch.util.convertToHttpResult
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ApiManager {
    companion object {
        private var INSTANCE: ApiManager? = null
        fun getInstance(): ApiManager =
            INSTANCE ?: ApiManager().also { INSTANCE = it }
    }

    private val httpClient = lazy { OkHttpClient() }
    private val retrofit = lazy {
        Retrofit.Builder()
            .baseUrl(BuildConfig.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(CoroutineCallAdapterFactory())
            .client(httpClient.value)
            .build()
    }

    suspend fun searchRepositories(keywords: String, sortBy: String, pageNumber: Int) =
        retrofit.value.create(GitHubApi::class.java)
            .searchRepositories(keywords, sortBy, pageNumber)
            .convertToHttpResult()

    @VisibleForTesting
    fun getHttpClient() = httpClient.value
}