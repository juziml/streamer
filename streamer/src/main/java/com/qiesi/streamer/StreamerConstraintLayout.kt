package com.qiesi.streamer

import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.content.Context
import android.content.res.Resources
import android.graphics.*
import android.util.AttributeSet
import android.util.Log
import android.view.animation.LinearInterpolator
import androidx.constraintlayout.widget.ConstraintLayout

private fun String.log(secondTag:String="") {
    if(BuildConfig.DEBUG){
        Log.i("--streamer", "$secondTag $this")
    }
}
private val Int.dp get() = (this * (0.5F + Resources.getSystem().displayMetrics.density)).toInt()
private val Float.dp get() = this * (0.5F + Resources.getSystem().displayMetrics.density)

/**
 * 流光viewGroup，继承ConstraintLayout
 * 在子view 绘制完成后 合成流光动画
 * 使用方法：[start] [stop]
 * @param context
 * @param attributeSet
 */
class StreamerConstraintLayout(context: Context, attributeSet: AttributeSet) :
    ConstraintLayout(context, attributeSet) {
//    init {
//        setWillNotDraw(false)//无背景时开启draw,很有用的方法，记录一下
//    }
    private val path = Path()
    private val xfermode = PorterDuffXfermode(PorterDuff.Mode.SRC_ATOP)
    private val streamerWidth = 30F.dp
    private val streamerHeightOffset = 0F
    private val angleSize:Double = 30.0
    private val paintStreamer = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.parseColor("#8adede")
    }

   private var progress = 0F

    private val floatAnim by lazy {
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
        refreshPath()
        paintStreamer.xfermode = xfermode
        canvas.drawPath(path, paintStreamer)
        paintStreamer.xfermode = null
        canvas.restoreToCount(count)
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        floatAnim.cancel()
    }


    private fun refreshPath() {
        path.reset()
        val startX = progress * width
        path.moveTo(startX, 0F)
        val cos = Math.cos(Math.toRadians(angleSize))
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