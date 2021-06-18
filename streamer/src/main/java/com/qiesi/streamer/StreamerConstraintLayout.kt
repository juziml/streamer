package com.qiesi.streamer

import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.content.Context
import android.content.res.Resources
import android.graphics.*
import android.util.AttributeSet
import android.util.Log
import android.view.animation.AccelerateInterpolator
import androidx.constraintlayout.widget.ConstraintLayout

private fun String.log(secondTag: String = "") {
    if (BuildConfig.DEBUG) {
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
class StreamerConstraintLayout(context: Context, attributeSet: AttributeSet?) :
    ConstraintLayout(context, attributeSet) {
    constructor(context: Context) : this(context, null)

    //    init {
//        setWillNotDraw(false)//无背景时开启draw,很有用的方法，记录一下
//    }
    private val DEFAULT_STREAMER_WIDTH = 30F.dp
    private val DEFAULT_ANGLE_SIZE = 30
    private val DEFAULT_COLOR = Color.parseColor("#F2F4F7")
    private val DEFAULT_DURATION = 2000L

    private val path = Path()
    private val xfermode = PorterDuffXfermode(PorterDuff.Mode.SRC_ATOP)
    var streamerWidth: Float
    var streamerHeightOffset: Float
    var angleSize: Int
    var streamerColor: Int
    var animDuration: Long

    private val paintStreamer = Paint(Paint.ANTI_ALIAS_FLAG)
    private var progress = 0F
    private val floatAnim by lazy {
        ObjectAnimator.ofFloat(0F, 1F).apply {
            repeatMode = ValueAnimator.RESTART
            repeatCount = ValueAnimator.INFINITE
            interpolator = AccelerateInterpolator()
            addUpdateListener {
                progress = it.animatedValue as Float
                postInvalidate()
            }
        }
    }

    init {
        if (null != attributeSet) {
            val typeArray =
                context.obtainStyledAttributes(attributeSet, R.styleable.StreamerConstraintLayout)
            streamerWidth = typeArray.getDimensionPixelOffset(
                R.styleable.StreamerConstraintLayout_sc_width,
                DEFAULT_STREAMER_WIDTH.toInt()
            ).toFloat()
            streamerHeightOffset =
                typeArray.getDimensionPixelOffset(
                    R.styleable.StreamerConstraintLayout_sc_heightOffset,
                    0
                ).toFloat()
            angleSize = typeArray.getInt(
                R.styleable.StreamerConstraintLayout_sc_angle,
                DEFAULT_ANGLE_SIZE.toInt()
            )
            streamerColor =
                typeArray.getColor(R.styleable.StreamerConstraintLayout_sc_color, DEFAULT_COLOR)
            animDuration = typeArray.getInt(
                R.styleable.StreamerConstraintLayout_sc_duration,
                DEFAULT_DURATION.toInt()
            ).toLong()
            typeArray.recycle()
        } else {
            streamerWidth = DEFAULT_STREAMER_WIDTH
            streamerHeightOffset = 0F
            angleSize = DEFAULT_ANGLE_SIZE
            streamerColor = DEFAULT_COLOR
            animDuration = DEFAULT_DURATION
        }
        paintStreamer.color = streamerColor

    }

    override fun dispatchDraw(canvas: Canvas) {
        val count = canvas.saveLayer(0F, 0F, width.toFloat(), height.toFloat(), null)
        super.dispatchDraw(canvas)//内容
        refreshPath()
        paintStreamer.xfermode = xfermode
        paintStreamer.color = streamerColor
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
        val cos = Math.cos(Math.toRadians(angleSize.toDouble()))
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
        floatAnim.duration = animDuration
        floatAnim.start()
    }

    fun stop() {
        floatAnim.cancel()

    }
}