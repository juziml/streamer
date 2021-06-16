package com.z.streamer.view

import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import android.view.animation.LinearInterpolator
import android.widget.LinearLayout
import com.z.streamer.dp
import com.z.streamer.log


class StreamerLinearLayout(context: Context, attributeSet: AttributeSet) :
    LinearLayout(context, attributeSet) {
    val path = Path()
    val xfermode = PorterDuffXfermode(PorterDuff.Mode.DST_ATOP)
    private val streamerWidth = 30F.dp
    private val streamerHeightOffset = 10F.dp

    private val paintStreamer = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        strokeCap = Paint.Cap.ROUND
        style = Paint.Style.FILL
        color = Color.parseColor("#FF0000")
    }
    private val mergePaint = Paint(Paint.ANTI_ALIAS_FLAG)

    private val contentBitmap by lazy {
        Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
    }

    var progress = 0.5F

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        "onSizeChanged".log("StreamerLinearLayout")
    }

    override fun draw(canvas: Canvas) {
        super.draw(canvas)
        "draw $canvas".log("StreamerLinearLayout")

    }
    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        "onDraw $canvas".log("StreamerLinearLayout")
        //开启离屏缓冲
//        val count = canvas.saveLayer(0F, 0F, width.toFloat(), height.toFloat(), null)
//        refreshPath(progress)
//        canvas.drawPath(path, paintStreamer)
//        mergePaint.xfermode = xfermode
//        canvas.drawBitmap(contentBitmap, 0F, 0F, mergePaint)//内容
//        mergePaint.xfermode = null
//        canvas.restoreToCount(count)

    }

    val floatAnim = ObjectAnimator.ofFloat(0F, 1F).apply {
        duration = 2000
        repeatMode = ValueAnimator.RESTART
        repeatCount = ValueAnimator.INFINITE
        interpolator = LinearInterpolator()
    }
    override fun dispatchDraw(canvas: Canvas) {
        super.dispatchDraw(canvas)
        "dispatchDraw $canvas".log("StreamerLinearLayout")
        refreshPath(progress)
        canvas.drawPath(path, paintStreamer)
    }

    override fun drawChild(canvas: Canvas?, child: View?, drawingTime: Long): Boolean {
        "drawChild $canvas".log("StreamerLinearLayout")
        return super.drawChild(canvas, child, drawingTime)
    }
    override fun onFinishInflate() {
        super.onFinishInflate()
        floatAnim.addUpdateListener {
            progress = it.animatedValue as Float
//            postInvalidate()
        }
        floatAnim.start()
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        floatAnim.cancel()
    }

    private fun refreshPath(progress: Float) {
        path.reset()
        val startX = progress * width
        path.moveTo(startX, 0F)
        path.lineTo(startX + streamerWidth, streamerHeightOffset)
        path.lineTo(startX + streamerWidth, height.toFloat())
        path.lineTo(startX, height - streamerHeightOffset)
        path.close()
    }
}