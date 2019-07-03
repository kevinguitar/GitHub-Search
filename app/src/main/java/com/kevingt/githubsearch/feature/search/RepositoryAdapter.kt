package com.kevingt.githubsearch.feature.search

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.kevingt.githubsearch.R
import com.kevingt.githubsearch.model.Repository
import com.kevingt.githubsearch.util.formatNumber
import com.kevingt.githubsearch.util.loadRoundImage
import kotlinx.android.synthetic.main.item_repository.view.*
import kotlinx.android.synthetic.main.layout_custom_circle_image_view.view.*

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
        if (viewType == TYPE_REPO) {
            return RepoViewHolder(LayoutInflater.from(parent.context).inflate(
                    R.layout.item_repository, parent, false))
        }
        return LoadMoreViewHolder(LayoutInflater.from(parent.context).inflate(
                R.layout.item_load_more, parent, false))
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is LoadMoreViewHolder) return
        (holder as RepoViewHolder).itemView.apply {
            iv_circle_avatar.loadRoundImage(repos[position].owner.avatarUrl)
            tv_repo_full_name.text = repos[position].fullName
            tv_repo_description.text = repos[position].description
            tv_repo_star_count.text = repos[position].stars.formatNumber()
            tv_repo_fork_count.text = repos[position].forks.formatNumber()

            // Hide the language tag if language parameter is empty
            if (repos[position].language.isNullOrEmpty()) {
                tv_repo_language.visibility = View.GONE
            } else {
                tv_repo_language.visibility = View.VISIBLE
                tv_repo_language.text = repos[position].language
            }
        }
    }

    interface ItemListener {
        fun onRepoClicked(repoUrl: String)
    }

    // ViewHolder inner classes
    inner class RepoViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        init {
            view.setOnClickListener {
                itemListener.onRepoClicked(repos[adapterPosition].url)
            }
        }
    }

    inner class LoadMoreViewHolder(view: View) : RecyclerView.ViewHolder(view)
}