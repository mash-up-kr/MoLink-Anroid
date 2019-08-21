package com.mashup.molink.utils

import android.content.Context
import android.content.SharedPreferences

object PrefUtil {

    const val PREF_NAME = "Pref"

    const val PREF_IS_FIRST_RUN = "is_first_run"

    private lateinit var pref: SharedPreferences

    fun getInstance(context: Context) {
        pref = context.applicationContext.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
    }

    fun get(key: String, defValue: String): String {
        return pref.getString(key, defValue)
    }

    fun get(key: String, defValue: Int): Int {
        return pref.getInt(key, defValue)
    }

    fun get(key: String, defValue: Boolean): Boolean {
        return pref.getBoolean(key, defValue)
    }

    fun put(key: String, value: String) {
        val editor = pref.edit()
        editor.putString(key, value)
        editor.apply()
    }

    fun put(key: String, value: Int) {
        val editor = pref.edit()
        editor.putInt(key, value)
        editor.apply()
    }

    fun put(key: String, value: Boolean) {
        val editor = pref.edit()
        editor.putBoolean(key, value)
        editor.apply()
    }

    fun remove(key: String) {
        val editor = pref.edit()
        editor.remove(key)
        editor.apply()
    }

    fun clear() {
        val editor = pref.edit()
        editor.clear()
        editor.apply()
    }
}