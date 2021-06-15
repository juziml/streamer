package com.z.streamer

import android.content.Context
import android.content.res.Resources
import android.graphics.*
import android.graphics.drawable.BitmapDrawable
import android.util.AttributeSet
import android.view.View

private val Int.dp get() = (this * (0.5F + Resources.getSystem().displayMetrics.density)).toInt()
private val Float.dp get() = this * (0.5F + Resources.getSystem().displayMetrics.density)


class StreamerView(context: Context, attributeSet: AttributeSet) : View(context, attributeSet) {

    private val paintTxt = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        textSize = 20F.dp
        color = Color.BLACK
    }

    private val bitmapContent = Bitmap.createBitmap(200.dp, 100.dp, Bitmap.Config.ARGB_8888).apply {
        density = Resources.getSystem().displayMetrics.density.toInt()
    }

    private val paintStreamer = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        strokeCap = Paint.Cap.ROUND
        style = Paint.Style.FILL
        color = Color.RED
    }
    private val mergePaint = Paint(Paint.ANTI_ALIAS_FLAG)

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)

    }

    init {
        val canvas = Canvas(bitmapContent).apply {
            density = 0
        }

        val txt = "十年可期"
        val txtWidth = paintTxt.measureText(txt)
        canvas.drawText(txt,0F, (bitmapContent.height/2).toFloat(),paintTxt)

        val img = BitmapFactory.decodeResource(context.resources
            ,R.drawable.ic_bar_chart,BitmapFactory.Options())//不能加载vector drawable
        val imgTop = bitmapContent.height /2F - (img.width /2F)
        canvas.drawBitmap(img,txtWidth,imgTop,paintTxt)
    }
    val path = Path().also {
        it.moveTo(10F.dp,0F)
        it.lineTo(20F.dp,0F)
        it.lineTo(20F.dp,100F.dp)
        it.lineTo(10F.dp,100F.dp)
        it.close()
    }
    val xfermode = PorterDuffXfermode(PorterDuff.Mode.DST_ATOP)
    //发现每次用的时候都搞不清楚谁往谁身上盖
    //如果A盖到B上，那么 先画B 然后选顶MODE，再画A （所以倒着写更舒服！）
    //xfermode 不只能合成“图片”，只要是“图形”应该都可以

   override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        //开启离屏缓冲
        val count = canvas.saveLayer(0F,0F,width.toFloat(),height.toFloat(),null)
//       canvas.drawBitmap(bitmapStreamer,40F,0F,mergePaint)//盖上流光
       canvas.drawPath(path,paintStreamer)
       mergePaint.xfermode = xfermode
       canvas.drawBitmap(bitmapContent,0F,0F,mergePaint)//内容
       mergePaint.xfermode = null
        canvas.restoreToCount(count)
    }
    private fun refreshPath(progress:Float){

    }
}