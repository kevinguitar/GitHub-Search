package com.kevingt.githubsearch.feature.search

import com.kevingt.githubsearch.base.BaseViewModelTest
import com.kevingt.githubsearch.model.ApiManager
import com.kevingt.githubsearch.model.HttpResult
import com.kevingt.githubsearch.model.Repository
import com.kevingt.githubsearch.model.SearchResult
import com.kevingt.githubsearch.util.Constants
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.ObsoleteCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Test
import retrofit2.Response

@ObsoleteCoroutinesApi
@ExperimentalCoroutinesApi
class SearchViewModelTest : BaseViewModelTest() {

    @MockK
    lateinit var apiManager: ApiManager

    lateinit var viewModel: SearchViewModel

    override fun init() {
        // Don't need ViewModelProviders because it's not relate to lifecycle
        viewModel = SearchViewModel(apiManager)
    }

    @Test
    fun searchRepositoriesSuccessTest() {
        coEvery { apiManager.searchRepositories(any(), any(), any()) } returns
                HttpResult.Success(Response.success(SearchResult()))

        viewModel.initSearch()
        runBlocking {
            viewModel.searchRepositories("fake_keywords", Constants.SORT_BY_BEST_MATCH)
        }

        coVerify(exactly = 1) { apiManager.searchRepositories(any(), any(), any()) }
        assertEquals(listOf<Repository>(), viewModel.repositories.value)
        assertEquals(false, viewModel.isLoading.value)
        assertEquals(null, viewModel.errorMessage.value)
    }

    @Test
    fun searchRepositoriesApiErrorTest() {
        val fakeMessage = "fake error message"

        coEvery { apiManager.searchRepositories(any(), any(), any()) } returns
                HttpResult.ApiError(fakeMessage)

        viewModel.initSearch()
        runBlocking {
            viewModel.searchRepositories("fake_keywords", Constants.SORT_BY_BEST_MATCH)
        }

        coVerify(exactly = 1) { apiManager.searchRepositories(any(), any(), any()) }
        assertEquals(false, viewModel.isLoading.value)
        assertEquals(fakeMessage, viewModel.errorMessage.value)
    }

    @Test
    fun searchRepositoriesNetworkErrorTest() {
        val fakeMessage = "fake error message"

        coEvery { apiManager.searchRepositories(any(), any(), any()) } returns
                HttpResult.NetworkError(Throwable(fakeMessage))

        viewModel.initSearch()
        runBlocking {
            viewModel.searchRepositories("fake_keywords", Constants.SORT_BY_BEST_MATCH)
        }

        coVerify(exactly = 1) { apiManager.searchRepositories(any(), any(), any()) }
        assertEquals(false, viewModel.isLoading.value)
        assertEquals(fakeMessage, viewModel.errorMessage.value)
    }
}