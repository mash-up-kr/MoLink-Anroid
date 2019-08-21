package com.mashup.molink.ui.register

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Toast
import com.gun0912.tedpermission.PermissionListener
import com.gun0912.tedpermission.TedPermission
import com.mashup.molink.ui.interest.InterestActivity
import com.mashup.molink.utils.Dlog
import kotlinx.android.synthetic.main.activity_register.*
import kotlinx.android.synthetic.main.activity_register.btnSignUp
import java.util.ArrayList
import android.provider.MediaStore
import android.database.Cursor
import android.graphics.Bitmap
import java.io.File
import android.graphics.BitmapFactory
import android.os.Build
import android.graphics.drawable.shapes.OvalShape
import android.graphics.drawable.ShapeDrawable
import android.util.Base64
import com.mashup.molink.R
import com.mashup.molink.utils.PrefUtil
import java.io.ByteArrayOutputStream
import java.util.regex.Pattern


class RegisterActivity : AppCompatActivity(), TextWatcher {

    private var passwordCheckValidate = false
    private  var idValidate = false
    private var passwordValidate = false

    private val PICK_FROM_ALBUM = 1
    private var tempFile: File? = null // 갤러리에서 선택한 파일
    private var originalBitmap:Bitmap? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        etID.addTextChangedListener(this)
        etPasswordCheck.addTextChangedListener(this)
        etPassword.addTextChangedListener(this)
        btnGallery.setOnClickListener {
            goToAlbum()
        }

        // 프로필 비트맵 초기화 : 사용자가 사진을 넣지 않을 경우 디폴트 사진이 존재함
        originalBitmap = BitmapFactory.decodeResource(applicationContext.resources, R.drawable.user_image)

        btnSignUp.setOnClickListener {
            if(etID.text.toString() == "" || etPassword.text.toString() == "" || etPasswordCheck.text.toString() == "" || etUserName.text.toString()==""){
                Toast.makeText(this@RegisterActivity, "항목을 모두 입력해주세요.", Toast.LENGTH_SHORT).show()
            } else if(!idValidate){
                Toast.makeText(this@RegisterActivity, "아이디를 다시 확인해주세요.", Toast.LENGTH_SHORT).show()
            } else if(!passwordValidate){
                Toast.makeText(this@RegisterActivity, "비밀번호를 다시 확인해주세요.", Toast.LENGTH_SHORT).show()
            } else if(!passwordCheckValidate){
                Toast.makeText(this@RegisterActivity, "비밀번호 일치 여부를 다시 확인해주세요.", Toast.LENGTH_SHORT).show()
            }
            else {
                Toast.makeText(this@RegisterActivity, "회원가입 되었습니다 :)", Toast.LENGTH_SHORT).show()

                PrefUtil.put(PrefUtil.PREF_IS_LOGIN, true)
                PrefUtil.put(PrefUtil.PREF_ID, etID.text.toString())
                PrefUtil.put(PrefUtil.PREF_PWD, etPassword.text.toString())
                PrefUtil.put(PrefUtil.PREF_USER_NAME, etUserName.text.toString())
                PrefUtil.put(PrefUtil.PREF_PROFILE_IMAGE, encodeToBase64(originalBitmap))

                var intent = Intent(this@RegisterActivity, InterestActivity::class.java)
                startActivity(intent)
                finish()
            }

        }

        tedPermission()
    }

    // bitmap to base64
    private fun encodeToBase64(image: Bitmap?): String {

        val byteStream = ByteArrayOutputStream()
        image?.compress(Bitmap.CompressFormat.PNG, 100, byteStream)
        val byteArr = byteStream.toByteArray()
        val imageEncoded = Base64.encodeToString(byteArr, Base64.DEFAULT)

        Dlog.d("image 값 : $image")
        Dlog.d("byteArr 값 : $byteArr")
        Dlog.d("imageEncoded 값 : $imageEncoded")

        return imageEncoded

    }

    //갤러리 권한 요청
    private fun tedPermission(){

        var permissionListener : PermissionListener = object : PermissionListener{
            override fun onPermissionGranted() {
                Dlog.d("갤러리 권한 허용")
            }
            override fun onPermissionDenied(deniedPermissions: ArrayList<String>?) {
                Dlog.d("갤러리 권한 거부")
            }

        }

        TedPermission.with(this)
            .setPermissionListener(permissionListener)
            //.setRationaleMessage("[설정] > [권한] 에서 권한을 허용할 수 있습니다.")
            //.setDeniedMessage("사진 및 파일을 저장하기 위하여 접근 권한이 필요합니다.")
            .setPermissions(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA)
            .check()
    }

    //갤러리 이동
    private fun goToAlbum(){
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = MediaStore.Images.Media.CONTENT_TYPE
        startActivityForResult(intent, PICK_FROM_ALBUM)
    }

    //갤러리에서 선택한 사진의 절대 경로 저장
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

        if (resultCode != Activity.RESULT_OK) {

            Toast.makeText(this, "취소 되었습니다.", Toast.LENGTH_SHORT).show()

            if(tempFile != null) {
                if (tempFile!!.exists()) {
                    if (tempFile!!.delete()) {
                        tempFile = null
                    }
                }
            }

            return
        }

        if (requestCode == PICK_FROM_ALBUM) {

            val photoUri = data!!.data
            var cursor: Cursor? = null

            try {
                /*
                *  Uri 스키마를
                *  content:/// 에서 file:/// 로  변경한다.
                */
                val proj = arrayOf(MediaStore.Images.Media.DATA)

                assert(photoUri != null)
                cursor = contentResolver.query(photoUri, proj, null, null, null)

                assert(cursor != null)
                val columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)

                cursor.moveToFirst()

                tempFile = File(cursor.getString(columnIndex))
            } finally {
                cursor?.close()
            }

            setImage()
        }
    }

    //갤러리에서 받아온 이미지 넣기
    private fun setImage(){
        val options = BitmapFactory.Options()
        originalBitmap = BitmapFactory.decodeFile(tempFile!!.absolutePath, options)

        Dlog.e("width:"+ originalBitmap!!.width.toString()+"height:"+ originalBitmap!!.height)
        imgViewUserImg.background = ShapeDrawable(OvalShape())
        if (Build.VERSION.SDK_INT >= 21) {
            imgViewUserImg.clipToOutline = true
        }

        imgViewUserImg.setImageBitmap(originalBitmap)
    }

    //유효성 검사
    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        //Dlog.d(s.toString())
        //비밀번호 일치 여부 검사
        if(etPasswordCheck.text.toString()!=""){
            if((etPassword.text.toString() != etPasswordCheck.text.toString())){
                tvPasswordCheckValidation.text = "비밀번호가 일치하지 않습니다 :("
                tvPasswordCheckValidation.setTextColor(Color.RED)

                passwordCheckValidate = false
            } else {
                //비밀번호가 일치할 경우
                tvPasswordCheckValidation.text = "비밀번호가 일치합니다 :)"
                tvPasswordCheckValidation.setTextColor(Color.BLUE)

                passwordCheckValidate = true
            }
        }
        if(etPassword.text.toString() == "" && etPasswordCheck.text.toString() == ""){
            tvPasswordCheckValidation.text = ""
        }

        //아이디 유효성 검사 : 영문 6자리 이상
        if(etID.text.toString() != ""){
            if(!Pattern.matches("^[a-zA-Z]*$", etID.text) || etID.text.length < 6){
                tvIDValidation.text = "아이디는 영문 6자 이상이어야 합니다."
                tvIDValidation.setTextColor(Color.RED)

                idValidate = false
            } else {
                tvIDValidation.text = ""
                idValidate = true
            }
        }

        //비밀번호 유효성 검사 : 영문 + 숫자 8자리 이상
        if(etPassword.text.toString() != ""){
            if(!Pattern.matches("^(?=.*[A-Za-z])(?=.*[0-9])[A-Za-z[0-9]]*$", etPassword.text) || etPassword.text.length < 8){
                tvPasswordValidation.text = "영문과 숫자 조합 8자 이상이어야 합니다."
                tvPasswordValidation.setTextColor(Color.RED)

                passwordValidate = false
            } else {
                tvPasswordValidation.text = ""
                passwordValidate = true
            }
        }
    }
    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
    }

    override fun afterTextChanged(s: Editable?) {

    }
}
