package com.mashup.molink.login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.jakewharton.rxbinding2.view.enabled
import com.mashup.molink.R
import com.mashup.molink.main.MainActivity
import com.mashup.molink.register.RegisterActivity
import com.mashup.molink.utils.Dlog
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_login.btnSignUp
import kotlinx.android.synthetic.main.activity_register.*

class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        //로그인 버튼 클릭 시
       btnLogin.setOnClickListener {
            if(etLoginID.text.toString() == "" || etLoginPassword.text.toString() == ""){
                Toast.makeText(this@LoginActivity, "아이디와 비밀번호 모두 입력해주세요 :)", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this@LoginActivity, "로그인 되었습니다 :)", Toast.LENGTH_SHORT).show()

                var intent = Intent(this@LoginActivity,MainActivity::class.java)
                startActivity(intent)
                finish()
            }

        }

        //회원가입 버튼 클릭 시
        btnSignUp.setOnClickListener {
            var intent = Intent(this@LoginActivity, RegisterActivity::class.java)
            startActivity(intent)
        }
    }
}
