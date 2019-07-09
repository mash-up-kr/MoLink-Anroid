package com.mashup.molink.utils

import android.content.Context

object ScreenUtil {

    fun getScreenWidth(context: Context)
            = context.applicationContext.resources.displayMetrics.widthPixels

    fun getScreenHeight(context: Context)
            = context.applicationContext.resources.displayMetrics.heightPixels
}