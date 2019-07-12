package com.mashup.molink.splash

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import com.mashup.molink.R
import com.mashup.molink.interest.InterestActivity
import com.mashup.molink.main.MainActivity
import com.mashup.molink.utils.Dlog
import org.jetbrains.anko.startActivityForResult

class SplashActivity : AppCompatActivity() {

    var isFirstRun = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_splash)
        Handler().postDelayed(splashhandler(), 2000)
    }

    inner class splashhandler: Runnable{

        override fun run() {
            var prefs = getSharedPreferences("Pref", Context.MODE_PRIVATE)

            isFirstRun = prefs.getBoolean("isFirstRun",true)

            Dlog.d("isFirstRun: " + isFirstRun)
            if(isFirstRun){
                var intent = Intent(this@SplashActivity, InterestActivity::class.java);
                startActivity(intent)
                finish()
            } else{
                var intent = Intent(this@SplashActivity, MainActivity::class.java);
                startActivity(intent)
                finish()
            }
        }
    }


}