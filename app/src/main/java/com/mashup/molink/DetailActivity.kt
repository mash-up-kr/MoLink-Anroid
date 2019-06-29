package com.mashup.molink

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.mashup.molink.utils.Dlog

class DetailActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Dlog.d("onCreate")
    }
}