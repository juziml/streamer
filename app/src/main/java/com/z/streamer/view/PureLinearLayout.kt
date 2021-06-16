package com.z.streamer.view

import android.content.Context
import android.graphics.Canvas
import android.util.AttributeSet
import android.view.View
import android.widget.LinearLayout
import com.z.streamer.log

class PureLinearLayout(context: Context, attributeSet: AttributeSet) :
    LinearLayout(context, attributeSet) {

    override fun dispatchDraw(canvas: Canvas?) {
        super.dispatchDraw(canvas)
        "dispatchDraw $canvas".log("PureLinearLayout")
    }

    override fun drawChild(canvas: Canvas?, child: View?, drawingTime: Long): Boolean {
        "drawChild $canvas".log("PureLinearLayout")
        return super.drawChild(canvas, child, drawingTime)
    }
}