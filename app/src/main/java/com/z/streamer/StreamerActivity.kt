package com.z.streamer

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.z.streamer.view.StreamerLinearLayout

class StreamerActivity : AppCompatActivity() {
    lateinit var sll: StreamerLinearLayout
    lateinit var sll2: StreamerLinearLayout
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_streamer)
        sll = findViewById(R.id.sll)
        sll2 = findViewById(R.id.sll2)
    }

    fun onStartBtnClick(v: View) {
        sll.start()
        sll2.start()
    }

    fun onStopBtnClick(v: View) {
        sll.stop()
        sll2.stop()
    }
}