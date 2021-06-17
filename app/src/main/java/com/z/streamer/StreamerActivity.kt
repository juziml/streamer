package com.z.streamer

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.qiesi.streamer.R
import com.qiesi.streamer.StreamerConstraintLayout

class StreamerActivity : AppCompatActivity() {
    lateinit var sc: StreamerConstraintLayout
    lateinit var sc2: StreamerConstraintLayout
    lateinit var sc3: StreamerConstraintLayout
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_streamer)
        sc = findViewById(R.id.sc)
        sc2 = findViewById(R.id.sc2)
        sc3 = findViewById(R.id.sc3)
    }

    fun onStartBtnClick(v: View) {
        sc.start()
        sc2.start()
        sc3.start()
    }

    fun onStopBtnClick(v: View) {
        sc.stop()
        sc2.stop()
        sc3.stop()
    }
}