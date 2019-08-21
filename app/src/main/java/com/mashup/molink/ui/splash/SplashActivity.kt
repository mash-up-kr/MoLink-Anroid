package com.mashup.molink.ui.splash

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import com.mashup.molink.R
import com.mashup.molink.ui.interest.InterestActivity
import com.mashup.molink.ui.login.LoginActivity
import com.mashup.molink.ui.main.MainActivity
import com.mashup.molink.utils.Dlog
import com.mashup.molink.utils.PrefUtil

class SplashActivity : AppCompatActivity() {

    var isFirstRun = false
    var isLogin = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_splash)
        Handler().postDelayed(splashhandler(), 2000)
    }

    inner class splashhandler : Runnable {

        override fun run() {

            isFirstRun = PrefUtil.get(PrefUtil.PREF_IS_FIRST_RUN, false)
            isLogin = PrefUtil.get(PrefUtil.PREF_IS_FIRST_RUN, false)
            Dlog.d("isFirstRun: $isFirstRun , isLogin : $isLogin")

            if (isLogin) {
                if (isFirstRun) {
                    val intent = Intent(this@SplashActivity, MainActivity::class.java);
                    startActivity(intent)
                    finish()
                } else {
                    val intent = Intent(this@SplashActivity, InterestActivity::class.java);
                    startActivity(intent)
                    finish()
                }
            } else {
                val intent = Intent(this@SplashActivity, LoginActivity::class.java);
                startActivity(intent)
                finish()
            }
        }
    }


}