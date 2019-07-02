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
import com.kevingt.githubsearch.util.Constants
import com.kevingt.githubsearch.util.hideKeyboard
import kotlinx.android.synthetic.main.activity_search.*

class SearchActivity : BaseActivity(), RepositoryAdapter.ItemListener {

    private lateinit var viewModel: SearchViewModel

    private val adapter = RepositoryAdapter(this)

    override fun getLayoutId() = R.layout.activity_search

    override fun initView(savedInstanceState: Bundle?) {
        viewModel = ViewModelProviders.of(this).get(SearchViewModel::class.java)

        // Provide three ways to trigger search
        //      1. Press ime action in keyboard
        //      2. Click search icon in the search bar
        //      3. Check chips to filter by star, fork or recent updated
        et_search_keywords.setOnEditorActionListener { _, _, _ ->
            performSearch()
            return@setOnEditorActionListener true
        }
        iv_search_icon.setOnClickListener {
            performSearch()
        }
        cg_search_sort.setOnCheckedChangeListener { _, _ ->
            performSearch()
        }

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
            } else {
                lav_search_loading.visibility = View.INVISIBLE
                cg_search_sort.visibility = View.VISIBLE
            }
        })

        viewModel.isLastPage.observe(this, Observer {
            adapter.isLastPage = it
        })

        viewModel.errorMessage.observe(this, Observer {
            showToast(it, Toast.LENGTH_LONG)
        })
    }

    private fun performSearch() {
        val keywords = et_search_keywords.text.toString()
        val sortBy = when (cg_search_sort.checkedChipId) {
            R.id.cp_search_stars -> Constants.SORT_BY_STARS
            R.id.cp_search_forks -> Constants.SORT_BY_FORKS
            R.id.cp_search_updated -> Constants.SORT_BY_UPDATED
            else -> Constants.SORT_BY_BEST_MATCH
        }

        if (keywords.isEmpty()) {
            showToast(R.string.search_empty_keywords)
            return
        }
        hideKeyboard()
        et_search_keywords.clearFocus()
        adapter.repos.clear()
        viewModel.initSearch()
        viewModel.searchRepositories(keywords, sortBy)
    }

    override fun onRepoClicked(repoUrl: String) {
        Intent(Intent.ACTION_VIEW).apply {
            data = Uri.parse(repoUrl)
        }.also { startActivity(it) }
    }
}
