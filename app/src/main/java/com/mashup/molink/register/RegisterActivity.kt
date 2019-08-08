package com.mashup.molink.register

import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Toast
import com.mashup.molink.R
import com.mashup.molink.main.MainActivity
import com.mashup.molink.utils.Dlog
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_register.*
import kotlinx.android.synthetic.main.activity_register.btnSignUp
import org.jetbrains.anko.startActivity

class RegisterActivity : AppCompatActivity(), TextWatcher {

    var passwordValidate = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        etPasswordCheck.addTextChangedListener(this)
        etPassword.addTextChangedListener(this)

        btnSignUp.setOnClickListener {
            if(etID.text.toString() == "" || etPassword.text.toString() == "" || etPasswordCheck.text.toString() == "" || etUserName.text.toString()==""){
                Toast.makeText(this@RegisterActivity, "항목을 모두 입력해주세요.", Toast.LENGTH_SHORT).show()
            } else if(!passwordValidate){
                Toast.makeText(this@RegisterActivity, "비밀번호 일치 여부를 다시 확인해주세요.", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this@RegisterActivity, "회원가입 되었습니다 :)", Toast.LENGTH_SHORT).show()


                var intent = Intent(this@RegisterActivity, MainActivity::class.java)
                startActivity(intent)
                finish()
            }

        }
    }

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        //Dlog.d(s.toString())
        if(etPasswordCheck.text.toString()!=""){
            if((etPassword.text.toString() != etPasswordCheck.text.toString())){
                tvPasswordValidation.text = "비밀번호가 일치하지 않습니다 :("
                tvPasswordValidation.setTextColor(Color.RED)

                passwordValidate = false
            } else {
                //비밀번호가 일치할 경우
                tvPasswordValidation.text = "비밀번호가 일치합니다 :)"
                tvPasswordValidation.setTextColor(Color.BLUE)

                passwordValidate = true
            }
        }
        if(etPassword.text.toString() == "" && etPasswordCheck.text.toString() == ""){
            tvPasswordValidation.text = ""
        }

    }
    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
    }

    override fun afterTextChanged(s: Editable?) {
    }
}
