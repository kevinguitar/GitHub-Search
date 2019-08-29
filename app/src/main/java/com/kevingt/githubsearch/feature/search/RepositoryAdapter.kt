package com.kevingt.githubsearch.feature.search

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.kevingt.githubsearch.R
import com.kevingt.githubsearch.databinding.ItemRepositoryBinding
import com.kevingt.githubsearch.model.Repository

class RepositoryAdapter(private val itemListener: ItemListener) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        private const val TYPE_REPO = 1
        private const val TYPE_LOAD_MORE = 2
    }

    var repos = listOf<Repository>()
    var isLastPage = true

    // If hasn't reach the last page, add a load more view to the end of repos
    override fun getItemCount() = if (isLastPage) repos.size else repos.size + 1

    override fun getItemViewType(position: Int) =
        if (position == repos.size) TYPE_LOAD_MORE else TYPE_REPO

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        if (viewType == TYPE_REPO) {
            return RepoViewHolder(
                DataBindingUtil.inflate(
                    inflater, R.layout.item_repository, parent, false
                )
            )
        }
        return LoadMoreViewHolder(inflater.inflate(R.layout.item_load_more, parent, false))
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is LoadMoreViewHolder) return
        (holder as RepoViewHolder).binding.apply {
            repo = repos[position]
            executePendingBindings()
        }
    }

    interface ItemListener {
        fun onRepoClicked(repoUrl: String)
    }

    // ViewHolder inner classes
    inner class RepoViewHolder(val binding: ItemRepositoryBinding) : RecyclerView.ViewHolder(binding.root) {
        init {
            binding.root.setOnClickListener {
                itemListener.onRepoClicked(repos[adapterPosition].url)
            }
        }
    }

    inner class LoadMoreViewHolder(view: View) : RecyclerView.ViewHolder(view)
}