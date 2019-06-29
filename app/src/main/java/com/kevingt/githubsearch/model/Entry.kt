package com.kevingt.githubsearch.model

import androidx.annotation.VisibleForTesting
import com.google.gson.annotations.SerializedName
import com.kevingt.githubsearch.util.Constants

data class SearchResult(
    @SerializedName("total_count")
    val count: Int,
    @SerializedName("items")
    val repos: List<Repository>
) {
    // Create to make unit test more easier
    @VisibleForTesting
    constructor() : this(0, listOf())

    // Judge if already reach last page
    fun isLastPage(pageNumber: Int): Boolean {
        return Constants.ITEMS_PER_PAGE * pageNumber >= count
    }
}

data class Repository(
    @SerializedName("full_name")
    val fullName: String,
    val owner: Owner,
    val description: String,
    @SerializedName("html_url")
    val url: String,
    @SerializedName("stargazers_count")
    val stars: Int,
    @SerializedName("forks_count")
    val forks: Int,
    val language: String
)

data class Owner(
    @SerializedName("avatar_url")
    val avatarUrl: String
)