package com.kevingt.githubsearch.feature.search

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.kevingt.githubsearch.R
import com.kevingt.githubsearch.base.BaseActivity
import com.kevingt.githubsearch.util.hideKeyboard
import kotlinx.android.synthetic.main.activity_search.*

class SearchActivity : BaseActivity(), RepositoryAdapter.ItemListener {

    private lateinit var viewModel: SearchViewModel

    private val adapter = RepositoryAdapter(this)

    override fun getLayoutId() = R.layout.activity_search

    override fun initView(savedInstanceState: Bundle?) {
        viewModel = ViewModelProviders.of(this).get(SearchViewModel::class.java)

        // Provide two ways to perform search
        et_search_keywords.setOnEditorActionListener { view, _, _ ->
            performSearch(view.text.toString())
            return@setOnEditorActionListener true
        }
        iv_search_icon.setOnClickListener {
            performSearch(et_search_keywords.text.toString())
        }

        //TODO: provide sort function

        // Setup recycler view and adapter
        rv_search_repository.layoutManager = LinearLayoutManager(this)
        rv_search_repository.adapter = adapter
        //TODO: add loadMore listener to RecyclerView

        // Observing live data in ViewModel
        viewModel.repositories.observe(this, Observer {
            if (it.isEmpty()) {
                // show no results hint
                iv_search_description_icon.setImageResource(R.drawable.icon_oops)
                tv_search_description_message.setText(R.string.search_content_description_no_results)
                gp_search_description.visibility = View.VISIBLE
                gp_search_result.visibility = View.INVISIBLE
            }
            adapter.repos.clear()
            adapter.repos.addAll(it)
            adapter.notifyDataSetChanged()
        })

        viewModel.isLoading.observe(this, Observer { isLoading ->
            if (isLoading) {
                // show loading view and hide other contents
                lav_search_loading.visibility = View.VISIBLE
                gp_search_description.visibility = View.INVISIBLE
                gp_search_result.visibility = View.INVISIBLE
            } else {
                lav_search_loading.visibility = View.INVISIBLE
                gp_search_result.visibility = View.VISIBLE
            }
        })

        viewModel.isLastPage.observe(this, Observer {
            adapter.isLastPage = it
        })

        viewModel.errorMessage.observe(this, Observer {
            showToast(it, Toast.LENGTH_LONG)
        })
    }

    private fun performSearch(keywords: String) {
        if (keywords.isEmpty()) {
            showToast(R.string.search_empty_keywords)
            return
        }
        hideKeyboard()
        et_search_keywords.clearFocus()
        adapter.repos.clear()
        viewModel.initSearch()
        viewModel.searchRepositories(keywords)
    }

    override fun onRepoClicked(repoUrl: String) {
        Intent(Intent.ACTION_VIEW).apply {
            data = Uri.parse(repoUrl)
        }.also { startActivity(it) }
    }
}
