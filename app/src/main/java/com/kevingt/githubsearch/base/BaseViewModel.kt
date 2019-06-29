package com.kevingt.githubsearch.base

import androidx.lifecycle.ViewModel
import com.kevingt.githubsearch.model.ApiManager
import kotlinx.coroutines.Job

abstract class BaseViewModel(apiManager: ApiManager? = null) : ViewModel() {

    protected val apiManager = apiManager ?: ApiManager.getInstance()
    protected val jobQueue = mutableListOf<Job>()

    override fun onCleared() {
        // Cancel the jobs that still working, and clear the queue
        jobQueue.forEach {
            it.takeIf(Job::isActive)?.cancel()
        }
        jobQueue.clear()
        super.onCleared()
    }
}