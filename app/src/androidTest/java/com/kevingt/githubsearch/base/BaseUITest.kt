package com.kevingt.githubsearch.base

import androidx.test.espresso.IdlingRegistry
import com.jakewharton.espresso.OkHttp3IdlingResource
import com.kevingt.githubsearch.model.ApiManager
import org.junit.Before

abstract class BaseUITest {

    @Before
    fun setIdlingResource() {
        // Set idling resource to let the test wait for async operation
        OkHttp3IdlingResource
            .create("OkHttp", ApiManager.getInstance().getHttpClient())
            .let {
                IdlingRegistry.getInstance().register(it)
            }
    }
}