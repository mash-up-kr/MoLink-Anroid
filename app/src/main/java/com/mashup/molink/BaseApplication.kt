package com.mashup.molink

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import android.provider.Settings
import com.mashup.molink.utils.Dlog
import com.mashup.molink.utils.PrefUtil

class BaseApplication: Application() {

    companion object {

        var DEBUG = false

        lateinit var androidId: String
    }

    @SuppressLint("HardwareIds")
    override fun onCreate() {
        super.onCreate()

        DEBUG = isDebuggable(this)

        androidId = Settings.Secure.getString(applicationContext.contentResolver, Settings.Secure.ANDROID_ID)
        Dlog.d("androidId : $androidId")

        PrefUtil.getInstance(this)
    }

    /**
     * get Debug Mode
     *
     * @param context
     * @return
     */
    private fun isDebuggable(context: Context): Boolean {

        var debuggable = false

        val pm = context.packageManager
        try {
            val appInfo = pm.getApplicationInfo(context.packageName, 0)
            debuggable = 0 != appInfo.flags and ApplicationInfo.FLAG_DEBUGGABLE
        } catch (e: PackageManager.NameNotFoundException) {
            /* debuggable variable will remain false */
        }

        return debuggable
    }
}