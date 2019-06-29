package com.mashup.molink

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.mashup.molink.utils.Dlog

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        Dlog.e("d")
        Dlog.d("New branch Lee")
        Dlog.d("Commit By Kwon")


    }
}
