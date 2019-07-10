package com.mashup.molink.splash

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.mashup.molink.R
import com.mashup.molink.interest.InterestActivity
import com.mashup.molink.main.MainActivity

class SplashActivity : AppCompatActivity() {



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_splash)

        var prefs = getSharedPreferences("Pref", Context.MODE_PRIVATE)

        var isFirstRun = prefs.getBoolean("isFirstRun",true)

        if(isFirstRun){
            var intent = Intent(this@SplashActivity, InterestActivity::class.java);
            startActivity(intent)
        } else{
            var intent = Intent(this@SplashActivity, MainActivity::class.java);
            startActivity(intent)
        }

        //Handler().postDelayed(splashhandler(isFirstRun), 3000)
        //prefs.edit().putBoolean("isFirstRun",false).apply()
    }
//
//    inner class splashhandler(isFirstRun: Boolean): Runnable{
//
//        val isFirstRun = isFirstRun
//
//        override fun run() {
//            if(!isFirstRun){
//                var intent = Intent(this@SplashActivity, InterestActivity::class.java);
//                startActivity(intent)
//            } else{
//                var intent = Intent(this@SplashActivity, MainActivity::class.java);
//                startActivity(intent)
//            }
//        }
//    }


}