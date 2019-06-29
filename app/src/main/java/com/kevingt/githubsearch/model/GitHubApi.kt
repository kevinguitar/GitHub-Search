package com.kevingt.githubsearch.model

import com.kevingt.githubsearch.util.Constants
import kotlinx.coroutines.Deferred
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface GitHubApi {

    @GET("repositories")
    fun searchRepositories(
        @Query("q") keywords: String,
        @Query("sort") sortBy: String,
        @Query("page") pageNumber: Int,
        @Query("per_page") perPage: Int = Constants.ITEMS_PER_PAGE
    ): Deferred<Response<SearchResult>>

}