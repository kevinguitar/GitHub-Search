package com.kevingt.githubsearch.util

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.kevingt.githubsearch.R
import com.kevingt.githubsearch.custom.CustomCircleImageView

object BindingAdapters {
    @BindingAdapter("roundImage")
    @JvmStatic
    fun CustomCircleImageView.setRoundImageResources(imgUrl: String) {
        findViewById<ImageView>(R.id.iv_circle_avatar).loadRoundImage(imgUrl)
    }
}