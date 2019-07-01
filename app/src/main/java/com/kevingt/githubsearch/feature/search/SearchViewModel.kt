package com.kevingt.githubsearch.feature.search

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.kevingt.githubsearch.base.BaseViewModel
import com.kevingt.githubsearch.model.ApiManager
import com.kevingt.githubsearch.model.HttpResult
import com.kevingt.githubsearch.model.Repository
import com.kevingt.githubsearch.util.Constants
import com.kevingt.githubsearch.util.addAllAndNotifyObserver
import com.kevingt.githubsearch.util.default
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SearchViewModel(apiManager: ApiManager? = null) : BaseViewModel(apiManager) {

    // Store the searching result, make sure View can't modify it
    val repositories: LiveData<List<Repository>>
        get() = _repositories
    private val _repositories = MutableLiveData<List<Repository>>().default(mutableListOf())

    val isLoading = MutableLiveData<Boolean>().default(false)
    val isLastPage = MutableLiveData<Boolean>().default(true)
    val errorMessage = MutableLiveData<String>()
    private var pageNumber = 1

    // Reset page number and clear the cache
    fun initSearch() {
        // Avoid adapter show load more view
        isLastPage.value = true
        pageNumber = 1
        _repositories.value = mutableListOf()
    }

    // Search repositories in IO thread and wait for the result
    fun searchRepositories(keywords: String, sortBy: String = Constants.SORT_BY_BEST_MATCH) {
        isLoading.value = true
        CoroutineScope(Dispatchers.IO).launch {
            val result = apiManager.searchRepositories(keywords, sortBy, pageNumber)
            withContext(Dispatchers.Main) {
                isLoading.value = false
                when (result) {
                    is HttpResult.Success -> {
                        val body = result.data.body()!!
                        _repositories.addAllAndNotifyObserver(body.repos)

                        // Judge the page number
                        if (body.isLastPage(pageNumber)) {
                            isLastPage.value = true
                        } else {
                            isLastPage.value = false
                            pageNumber + 1
                        }
                    }
                    is HttpResult.ApiError -> {
                        errorMessage.value = result.message
                    }
                    is HttpResult.NetworkError -> {
                        errorMessage.value = result.exception.message
                    }
                }
            }
        }.also { jobQueue.add(it) }
    }
}