package com.z.streamer.view

import android.content.Context
import android.graphics.Canvas
import android.util.AttributeSet
import android.view.View
import com.z.streamer.log

class PureView(context: Context, attributeSet: AttributeSet) : View(context, attributeSet) {

    override fun draw(canvas: Canvas?) {
        super.draw(canvas)
//        "draw $canvas".log("PureView")
    }
    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
//        "onDraw $canvas".log("PureView")
    }
}