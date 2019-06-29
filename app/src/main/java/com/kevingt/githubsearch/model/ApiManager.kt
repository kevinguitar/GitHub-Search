package com.kevingt.githubsearch.model

import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.kevingt.githubsearch.BuildConfig
import com.kevingt.githubsearch.util.Constants
import com.kevingt.githubsearch.util.convertToHttpResult
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ApiManager {
    companion object {
        private var INSTANCE: ApiManager? = null
        fun getInstance(): ApiManager =
            INSTANCE ?: ApiManager().also { INSTANCE = it }
    }

    private val retrofit = lazy {
        Retrofit.Builder()
            .baseUrl(BuildConfig.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(CoroutineCallAdapterFactory())
            .build()
    }

    suspend fun searchRepositories(
        keywords: String,
        sortBy: String = Constants.SORT_BY_BEST_MATCH,
        page: Int
    ) {
        retrofit.value.create(GitHubApi::class.java)
            .searchRepositories(keywords, sortBy, page)
            .convertToHttpResult()
    }
}