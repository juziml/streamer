package com.z.streamer

import android.content.Context
import android.content.res.Resources
import android.graphics.*
import android.util.AttributeSet
import android.view.View

private val Int.dp get() = (this * (0.5F + Resources.getSystem().displayMetrics.density)).toInt()
private val Float.dp get() = this * (0.5F + Resources.getSystem().displayMetrics.density)


class StreamerView(context: Context, attributeSet: AttributeSet) : View(context, attributeSet) {

    private val paintTxt = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        textSize = 20F.dp
        color = Color.BLACK
    }

    private val bitmapContent = Bitmap.createBitmap(100.dp, 100.dp, Bitmap.Config.ARGB_8888).apply {
        density = Resources.getSystem().displayMetrics.density.toInt()
    }

    private val paintStreamer = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        strokeCap = Paint.Cap.ROUND
        style = Paint.Style.FILL
        color = Color.RED
    }
    private val mergePaint = Paint(Paint.ANTI_ALIAS_FLAG)

    private val bitmapStreamer = Bitmap.createBitmap(100.dp, 100.dp, Bitmap.Config.ARGB_8888).apply {
        density = Resources.getSystem().displayMetrics.density.toInt()
    }

    init {
        val canvas = Canvas(bitmapContent).apply {
            density = 0
        }
        val rectF = RectF()
        rectF.left = 20F.dp
        rectF.right = rectF.left + 100F.dp
        rectF.top = 20F.dp
        rectF.bottom = rectF.top + 20F.dp
//        canvas.drawRect(rectF,paintTxt)
        canvas.drawText("十年可期",0F, (bitmapContent.height/2).toFloat(),paintTxt)

        val canvasStreamer = Canvas(bitmapStreamer).apply {
            density = 0
        }
        val path = Path()
        path.moveTo(10F.dp,0F)
        path.lineTo(20F.dp,0F)
        path.lineTo(20F.dp,100F.dp)
        path.lineTo(10F.dp,100F.dp)
        path.close()
        canvasStreamer.drawPath(path,paintStreamer)
    }
    val xfermode = PorterDuffXfermode(PorterDuff.Mode.DST_ATOP)

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        //开启离屏缓冲
        val count = canvas.saveLayer(0F,0F,width.toFloat(),height.toFloat(),null)
        canvas.drawBitmap(bitmapStreamer,40F,0F,mergePaint)
        mergePaint.xfermode = xfermode
        canvas.drawBitmap(bitmapContent,0F,0F,mergePaint)
        mergePaint.xfermode = null
        canvas.restoreToCount(count)
    }
}