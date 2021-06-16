package com.z.streamer

import android.content.res.Resources
import android.util.Log

fun String.log(secondTag:String="") {
    Log.i("----------streamer", "$secondTag $this")
}
val Int.dp get() = (this * (0.5F + Resources.getSystem().displayMetrics.density)).toInt()
val Float.dp get() = this * (0.5F + Resources.getSystem().displayMetrics.density)

