package com.mashup.molink

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.mashup.molink.utils.Dlog

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        Dlog.d("onCreate")
        Dlog.d("onCreate2")
        Dlog.d("onCreate3")
        Dlog.d("onCreate4")
        Dlog.d("onCreate5")

    }
}
