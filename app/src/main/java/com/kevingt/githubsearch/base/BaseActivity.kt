package com.kevingt.githubsearch.base

import android.os.Bundle
import android.widget.Toast
import androidx.annotation.LayoutRes
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar

abstract class BaseActivity : AppCompatActivity() {

    private lateinit var toast: Toast

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(getLayoutId())
        initView(savedInstanceState)
    }

    @LayoutRes
    protected abstract fun getLayoutId(): Int

    protected abstract fun initView(savedInstanceState: Bundle?)

    /**
     * @param toolbar   Allow to set custom Toolbar
     * @param titleId   The title that show on Toolbar
     */
    protected fun setActionBar(toolbar: Toolbar, @StringRes titleId: Int) {
        setSupportActionBar(toolbar)
        supportActionBar?.let { setTitle(titleId) }
    }

    /**
     * @param resId     Message id in strings.xml
     * @param duration  The time that the toast will showing
     */
    protected fun showToast(@StringRes resId: Int, duration: Int = Toast.LENGTH_SHORT) {
        if (::toast.isInitialized) {
            toast.setText(resId)
            toast.duration = duration
            toast.show()
            return
        }
        toast = Toast.makeText(this, resId, duration)
        toast.show()
    }
}