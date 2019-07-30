package com.mashup.molink.utils

import android.content.Context
import androidx.core.content.ContextCompat
import com.mashup.molink.R

object ColorUtil {

    fun getColors(context: Context): Array<String> {

        val applicationContext = context.applicationContext

        return arrayOf(
            String.format("#%06X", 0xFFFFFF and ContextCompat.getColor(applicationContext, R.color.purpleish_blue)),
            String.format("#%06X", 0xFFFFFF and ContextCompat.getColor(applicationContext, R.color.maize)),
            String.format("#%06X", 0xFFFFFF and ContextCompat.getColor(applicationContext, R.color.lightblue)),
            String.format("#%06X", 0xFFFFFF and ContextCompat.getColor(applicationContext, R.color.lighter_purple)),
            String.format("#%06X", 0xFFFFFF and ContextCompat.getColor(applicationContext, R.color.lightish_green)),
            String.format("#%06X", 0xFFFFFF and ContextCompat.getColor(applicationContext, R.color.pig_pink))
        )
    }
}