package com.mashup.molink.ui.login

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.mashup.molink.R
import com.mashup.molink.ui.main.MainActivity
import com.mashup.molink.ui.register.RegisterActivity
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_login.btnSignUp

class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        var prefs = getSharedPreferences("Pref", Context.MODE_PRIVATE)

        //로그인 버튼 클릭 시
       btnLogin.setOnClickListener {
            if(etLoginID.text.toString() == "" || etLoginPassword.text.toString() == ""){
                Toast.makeText(this@LoginActivity, "아이디와 비밀번호 모두 입력해주세요 :)", Toast.LENGTH_SHORT).show()
            } else {
                if(etLoginID.text.toString()==prefs.getString("id", "") && etLoginPassword.text.toString()==prefs.getString("pwd", "")){
                    Toast.makeText(this@LoginActivity, "로그인 되었습니다 :)", Toast.LENGTH_SHORT).show()
                    var intent = Intent(this@LoginActivity,MainActivity::class.java)
                    prefs.edit().putBoolean("isLogin", true).apply()
                    startActivity(intent)
                    finish()
                }else{
                    Toast.makeText(this@LoginActivity, "아이디와 비밀번호가 일치하지 않습니다.", Toast.LENGTH_SHORT).show()
                }

            }

        }

        //회원가입 버튼 클릭 시
        btnSignUp.setOnClickListener {
            var intent = Intent(this@LoginActivity, RegisterActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}
