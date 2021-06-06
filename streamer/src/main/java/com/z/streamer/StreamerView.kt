package com.z.streamer

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View

class StreamerView(context: Context,attributeSet: AttributeSet)
    : View(context,attributeSet) {

    val paint = Paint(Paint.ANTI_ALIAS_FLAG)

    init {

    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

    }
}