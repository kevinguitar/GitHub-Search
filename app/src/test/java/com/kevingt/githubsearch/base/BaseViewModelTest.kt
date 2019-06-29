package com.kevingt.githubsearch.base

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import io.mockk.MockKAnnotations
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.ObsoleteCoroutinesApi
import kotlinx.coroutines.newSingleThreadContext
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Rule

/**
 *      I'd wrote an article to share how to write unit test for using ViewModel x Coroutines,
 *      because there're still not much information on the internet. The link is attached below.
 *
 *      https://link.medium.com/uzj8ASqzUX
 */

@ObsoleteCoroutinesApi
@ExperimentalCoroutinesApi
abstract class BaseViewModelTest {

    //Make sure the LiveData operations only occur on Main thread
    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    // Create a fake UI thread
    private val mainThreadSurrogate = newSingleThreadContext("UI thread")

    abstract fun init()

    @Before
    fun setUp() {
        // Let the fake UI thread replaced Main thread
        Dispatchers.setMain(mainThreadSurrogate)
        MockKAnnotations.init(this)
        init()
    }

    @After
    fun cleanUp() {
        // Reset the Main thread when the test is finished
        Dispatchers.resetMain()
        mainThreadSurrogate.close()
    }
}