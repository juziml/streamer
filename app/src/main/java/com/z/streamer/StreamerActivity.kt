package com.z.streamer

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.qiesi.streamer.R
import com.qiesi.streamer.StreamerConstraintLayout

class StreamerActivity : AppCompatActivity() {
    lateinit var sc: StreamerConstraintLayout
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_streamer)
        sc = findViewById(R.id.sc)
    }

    fun onStartBtnClick(v: View) {
        sc.start()
    }

    fun onStopBtnClick(v: View) {
        sc.stop()
    }
}