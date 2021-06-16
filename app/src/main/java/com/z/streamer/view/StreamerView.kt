package com.z.streamer.view

import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import android.view.animation.LinearInterpolator
import com.z.streamer.R
import com.z.streamer.dp
import com.z.streamer.log

class StreamerView(context: Context, attributeSet: AttributeSet) : View(context, attributeSet) {
    val path = Path()
    val xfermode = PorterDuffXfermode(PorterDuff.Mode.DST_ATOP)
    private val streamerWidth = 30F.dp
    private val streamerHeightOffset = streamerWidth

    private val imgWidth = 30.dp
    private val txtHeightOffset = 4F.dp

    private val paintTxt = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        textSize = 16F.dp
        typeface = Typeface.DEFAULT_BOLD
        color = Color.parseColor("#dbdbdb")
    }

    private val paintStreamer = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        strokeCap = Paint.Cap.ROUND
        style = Paint.Style.FILL
        color = Color.parseColor("#8adede")
    }
    private val mergePaint = Paint(Paint.ANTI_ALIAS_FLAG)

    private val contentBitmap by lazy {
        Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
    }

    var progress = 0F

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        "onSizeChanged".log("StreamerView")
        val canvas = Canvas(contentBitmap).apply {
            density = 0
        }
        val txt = "等等呗 "
        val options = BitmapFactory.Options()
        options.inJustDecodeBounds = true
        BitmapFactory.decodeResource(
            context.resources, R.drawable.ic_cup, options
        )//不可以加载Vector资源
        options.inJustDecodeBounds = false
        options.inDensity = options.outWidth
        options.inTargetDensity = imgWidth
        val img = BitmapFactory.decodeResource(
            context.resources, R.drawable.ic_cup, options
        )
        val txtWidth = paintTxt.measureText(txt)
        val textBounds = Rect()
        paintTxt.getTextBounds(txt, 0, txt.length, textBounds)

        val txtY = (height / 2F) - (textBounds.top + textBounds.bottom) / 2 + txtHeightOffset
        val startX = (width / 2) - (txtWidth + img.width) / 2F
        canvas.drawText(txt, startX, txtY, paintTxt)

        val imgTop = height / 2F - (img.height / 2F)
        canvas.drawBitmap(img, startX + txtWidth, imgTop, paintTxt)

    }


    //发现每次用的时候都搞不清楚谁往谁身上盖
    //一般都是双图合成，为了能选对MODE,先画表层Destination img 后画底层source img (反正来就对了)
    //Xfermode 不只能合成“图片”，只要是“图形”应该都可以
    //不能加载vector drawable

    override fun onDraw(canvas: Canvas) {
        //开启离屏缓冲
        val count = canvas.saveLayer(0F, 0F, width.toFloat(), height.toFloat(), null)
        refreshPath(progress)
        canvas.drawPath(path, paintStreamer)
        mergePaint.xfermode = xfermode
        canvas.drawBitmap(contentBitmap, 0F, 0F, mergePaint)//内容
        mergePaint.xfermode = null
        canvas.restoreToCount(count)

    }

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

    override fun onFinishInflate() {
        super.onFinishInflate()
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