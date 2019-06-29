package com.kevingt.githubsearch.model

import kotlinx.coroutines.Deferred
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface GitHubApi {

    @GET("repositories")
    fun searchRepositories(
        @Query("q") keywords: String,
        @Query("sort") sortBy: String,
        @Query("page") page: Int
    ): Deferred<Response<SearchResult>>

}