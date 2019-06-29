package com.kevingt.githubsearch.model

import com.google.gson.annotations.SerializedName

data class SearchResult(
    @SerializedName("total_count")
    val count: Int,
    @SerializedName("items")
    val repos: List<Repository>
) {
    //TODO: provide a function to judge is last page or not
    //fun isLastPage(): Boolean
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