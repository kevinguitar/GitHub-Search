package com.kevingt.githubsearch.util

import android.app.Activity
import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.ImageView
import androidx.lifecycle.MutableLiveData
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.kevingt.githubsearch.model.HttpResult
import kotlinx.coroutines.Deferred
import retrofit2.Response


/**
 * @param initialValue  Set default value for LiveData
 */
fun <T : Any?> MutableLiveData<T>.default(initialValue: T) = apply { value = initialValue }


/**
 * @return  Solving the problem for list modification that won't notify observer
 */
fun <T> MutableLiveData<List<T>>.addAllAndNotifyObserver(items: List<T>) {
    val updatedItems = value as MutableList
    updatedItems.addAll(items)
    value = updatedItems
}


/**
 * @return  Convert dp to px quickly
 */
fun Int.toPx(context: Context) = this * context.resources.displayMetrics.density


/**
 * @return  Avoid showing integer too long on view
 */
fun Int.formatNumber(): String {
    if (this < 1000) return toString()
    // Subtract the remainder of 100. Ex: 1021 -> 1000
    val round = minus(rem(100))
    if (round.rem(1000) == 0) {
        return round.div(1000).toString() + "k"
    }
    return "%.1fk".format(round.div(1000f))
}


/**
 * @param url   Load image url and crop to circle using Glide
 */
fun ImageView.loadRoundImage(url: String) {
    Glide.with(this)
            .load(url)
            .apply(RequestOptions.circleCropTransform())
            .into(this)
}


/**
 * @return  Only works for activities
 */
fun Activity.hideKeyboard() {
    val imm = getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
    val view = currentFocus ?: View(this)
    imm.hideSoftInputFromWindow(view.windowToken, 0)
}


/**
 * @return  HttpResult: convert from Response, to be more easy to handle
 */
suspend fun <T> Deferred<Response<T>>.convertToHttpResult(): HttpResult<Response<T>> {
    return try {
        val result = await()
        if (result.isSuccessful) {
            HttpResult.Success(result)
        } else {
            HttpResult.ApiError(result.message())
        }
    } catch (e: Throwable) {
        HttpResult.NetworkError(e)
    }
}