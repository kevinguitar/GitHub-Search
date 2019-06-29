package com.kevingt.githubsearch.base

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.Job

abstract class BaseViewModel : ViewModel() {

    protected var jobQueue = mutableListOf<Job>()

    override fun onCleared() {
        // Cancel the jobs that still working, and clear the queue
        jobQueue.forEach {
            it.takeIf(Job::isActive)?.cancel()
        }
        jobQueue.clear()
        super.onCleared()
    }
}