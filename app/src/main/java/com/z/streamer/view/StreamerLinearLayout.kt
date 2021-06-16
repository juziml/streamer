package com.z.streamer.view

import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import android.view.animation.LinearInterpolator
import android.widget.LinearLayout
import androidx.constraintlayout.widget.ConstraintLayout
import com.z.streamer.dp
import com.z.streamer.log


class StreamerLinearLayout(context: Context, attributeSet: AttributeSet) :
    ConstraintLayout(context, attributeSet) {
    val path = Path()
    val xfermode = PorterDuffXfermode(PorterDuff.Mode.SRC_ATOP)

    private val streamerWidth = 30F.dp
    private val streamerHeightOffset = streamerWidth

    private val paintStreamer = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.parseColor("#8adede")
    }

    var progress = 0F

    val floatAnim by lazy {
        ObjectAnimator.ofFloat(0F, 1F).apply {
            duration = 2000
            repeatMode = ValueAnimator.RESTART
            repeatCount = ValueAnimator.INFINITE
            interpolator = LinearInterpolator()
            addUpdateListener {
                progress = it.animatedValue as Float
                postInvalidate()
            }
        }
    }

    override fun dispatchDraw(canvas: Canvas) {
        val count = canvas.saveLayer(0F, 0F, width.toFloat(), height.toFloat(), null)
        super.dispatchDraw(canvas)//内容
        refreshPath(progress)
        paintStreamer.xfermode = xfermode
        canvas.drawPath(path, paintStreamer)
        paintStreamer.xfermode = null
        canvas.restoreToCount(count)
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        floatAnim.cancel()
    }


    private fun refreshPath(progress: Float) {
        path.reset()
        val startX = progress * width
        path.moveTo(startX, 0F)
        val cos = Math.cos(Math.toRadians(45.0))
        val c = Math.abs(streamerWidth / cos)
        val num = c * c - (streamerWidth * streamerWidth)
        //y轴 长度
        val b = Math.sqrt(num).toFloat() + streamerHeightOffset
        path.lineTo(startX + streamerWidth, b)
        path.lineTo(startX + streamerWidth, height.toFloat())
        path.lineTo(startX, height - b)
        path.close()
    }

    fun start() {
        floatAnim.start()
    }

    fun stop() {
        floatAnim.cancel()

    }
}