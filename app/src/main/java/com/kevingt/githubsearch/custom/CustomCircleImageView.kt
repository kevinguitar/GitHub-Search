package com.kevingt.githubsearch.custom

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import androidx.core.content.ContextCompat
import com.kevingt.githubsearch.R
import com.kevingt.githubsearch.util.toPx

class CustomCircleImageView @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyle: Int = 0
) : FrameLayout(context, attrs, defStyle) {

    private val strokeSize = 1.toPx(context)
    private val paint = Paint().apply {
        isAntiAlias = true
        style = Paint.Style.STROKE
        strokeWidth = strokeSize
        color = ContextCompat.getColor(context, R.color.colorPrimary)
    }

    init {
        LayoutInflater.from(context).inflate(
                R.layout.layout_custom_circle_image_view, this, true)
    }

    override fun dispatchDraw(canvas: Canvas?) {
        super.dispatchDraw(canvas)
        val radius = width / 2f
        canvas?.drawCircle(radius, radius, radius - strokeSize, paint)
    }

}