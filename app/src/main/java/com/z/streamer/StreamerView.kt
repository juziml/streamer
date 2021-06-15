package com.z.streamer

import android.content.Context
import android.content.res.Resources
import android.graphics.*
import android.graphics.drawable.BitmapDrawable
import android.util.AttributeSet
import android.util.Log
import android.view.View

private val Int.dp get() = (this * (0.5F + Resources.getSystem().displayMetrics.density)).toInt()
private val Float.dp get() = this * (0.5F + Resources.getSystem().displayMetrics.density)

fun String.log() {
    Log.i("streamer", this)
}

class StreamerView(context: Context, attributeSet: AttributeSet) : View(context, attributeSet) {

    private val paintTxt = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        textSize = 20F.dp
        color = Color.BLACK
    }

    private val paintStreamer = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        strokeCap = Paint.Cap.ROUND
        style = Paint.Style.FILL
        color = Color.RED
    }
    private val mergePaint = Paint(Paint.ANTI_ALIAS_FLAG)

    private val contentBitmap by lazy {
        Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        "onSizeChanged".log()
        val canvas = Canvas(contentBitmap).apply {
            density = 0
        }
        val txt = "十年可期"
        val img = BitmapFactory.decodeResource(
            context.resources, R.drawable.ic_bar_chart, BitmapFactory.Options()
        )
        val txtWidth = paintTxt.measureText(txt)
        canvas.drawText(txt, 0F, height / 2F, paintTxt)
        val imgTop = height / 2F - (img.height / 2F)
        canvas.drawBitmap(img, txtWidth, imgTop, paintTxt)

    }


    val path = Path().also {
        it.moveTo(10F.dp, 0F)
        it.lineTo(40F.dp, 0F)
        it.lineTo(40F.dp, 100F.dp)
        it.lineTo(10F.dp, 100F.dp)
        it.close()
    }

    val xfermode = PorterDuffXfermode(PorterDuff.Mode.DST_ATOP)

    //发现每次用的时候都搞不清楚谁往谁身上盖
    //一般都是双图合成，为了能选对MODE,先画表层Destination img 后画底层source img (反正来就对了)
    //Xfermode 不只能合成“图片”，只要是“图形”应该都可以
    //不能加载vector drawable

    override fun onDraw(canvas: Canvas) {
        //开启离屏缓冲
        val count = canvas.saveLayer(0F,0F,width.toFloat(),height.toFloat(),null)
        canvas.drawPath(path,paintStreamer)
        mergePaint.xfermode = xfermode
        canvas.drawBitmap(contentBitmap,0F,0F,mergePaint)//内容
        mergePaint.xfermode = null
        canvas.restoreToCount(count)
    }

    private fun refreshPath(progress: Float) {

    }
}