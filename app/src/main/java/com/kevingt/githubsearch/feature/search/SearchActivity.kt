package com.kevingt.githubsearch.feature.search

import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.kevingt.githubsearch.R
import com.kevingt.githubsearch.base.BaseActivity

class SearchActivity : BaseActivity() {

    private lateinit var viewModel: SearchViewModel

    override fun getLayoutId() = R.layout.activity_search

    override fun initView(savedInstanceState: Bundle?) {
        viewModel = ViewModelProviders.of(this).get(SearchViewModel::class.java)

        viewModel.repositories.observe(this, Observer {
            //TODO: add data into adapter and notify changes
        })

        viewModel.isLoading.observe(this, Observer {
            //TODO: show/hide the loading view
        })

        viewModel.errorMessage.observe(this, Observer {
            //TODO: display error message
        })
    }
}
