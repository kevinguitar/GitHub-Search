package com.kevingt.githubsearch.base

import android.os.Bundle
import android.widget.Toast
import androidx.annotation.LayoutRes
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import com.kevingt.githubsearch.R

abstract class BaseActivity : AppCompatActivity() {

    private lateinit var toast: Toast

    override fun onCreate(savedInstanceState: Bundle?) {
        // Reset the theme to NoActionBar
        setTheme(R.style.AppTheme_NoActionBar)
        super.onCreate(savedInstanceState)
        setContentView(getLayoutId())
        initView(savedInstanceState)
    }

    @LayoutRes
    protected abstract fun getLayoutId(): Int

    protected abstract fun initView(savedInstanceState: Bundle?)

    /**
     * @param resId     Message id in strings.xml
     * @param duration  The time that the toast will showing
     */
    protected fun showToast(@StringRes resId: Int, duration: Int = Toast.LENGTH_SHORT) {
        showToast(getString(resId), duration)
    }

    protected fun showToast(message: String, duration: Int = Toast.LENGTH_SHORT) {
        if (::toast.isInitialized) {
            toast.setText(message)
            toast.duration = duration
            toast.show()
            return
        }
        toast = Toast.makeText(this, message, duration)
        toast.show()
    }
}