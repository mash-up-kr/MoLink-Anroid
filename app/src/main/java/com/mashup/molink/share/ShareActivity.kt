package com.mashup.molink.share

import android.app.Activity
import android.content.Intent
import android.os.AsyncTask
import android.os.Build
import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import android.view.MotionEvent
import android.view.View
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import com.mashup.molink.R
import com.mashup.molink.main.MainActivity
import com.mashup.molink.utils.Dlog
import kotlinx.android.synthetic.main.activity_share.*
import org.jsoup.Jsoup

class ShareActivity : AppCompatActivity() {

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_share)

        tvSharedUrl.movementMethod = ScrollingMovementMethod()

        getLinkUrl()

        var hashTagsArray =arrayListOf<String>()
        //링크 저장 버튼 클릭시
        btnLinkSave.setOnClickListener {

            var intent = Intent(this@ShareActivity, MainActivity::class.java)
            intent.putExtra("sharedUrl", tvSharedUrl.text)
            intent.putExtra("sharedTitle", etSharedTitle.text)
            intent.putExtra("hashTags",hashTagsArray)
            setResult(Activity.RESULT_OK, intent)

            finish()
        }

        // x 버튼 클릭 시
        btnShareActivityCancel.setOnClickListener{
            finish()
        }

        //제목 수정
        etSharedTitle.focusable = View.NOT_FOCUSABLE
        etSharedTitle.isClickable = false

        etSharedTitle.setOnTouchListener(object : View.OnTouchListener {
            override fun onTouch(v: View?, event: MotionEvent?): Boolean {
                val DRAWABLE_LEFT = 0
                val DRAWABLE_TOP = 1
                val DRAWABLE_RIGHT = 2
                val DRAWABLE_BOTTOM = 3

                if(event!!.action == MotionEvent.ACTION_UP) {
                    if(event.rawX >= (etSharedTitle.right - etSharedTitle.compoundDrawables[DRAWABLE_RIGHT].bounds.width())) {
                        etSharedTitle.focusable = View.FOCUSABLE
                        etSharedTitle.isFocusableInTouchMode = true

                        return true
                    }
                }
                return false
            }
        })


        //해쉬태그 추가

        //recycler view
        recyclerviewHashTags.adapter = HashTagAdapter(hashTagsArray)
        recyclerviewHashTags.layoutManager = GridLayoutManager(this, 3) //예쁘게 나중에

        edAddHashTag.setOnTouchListener(object : View.OnTouchListener {
            override fun onTouch(v: View?, event: MotionEvent?): Boolean {
                val DRAWABLE_LEFT = 0
                val DRAWABLE_TOP = 1
                val DRAWABLE_RIGHT = 2
                val DRAWABLE_BOTTOM = 3

                if(event!!.action == MotionEvent.ACTION_UP) {
                    if(event.rawX >= (edAddHashTag.right - edAddHashTag.compoundDrawables[DRAWABLE_RIGHT].bounds.width())) {
                        var hashtagText = edAddHashTag.text.toString()
                        Dlog.e(hashtagText)
                        if(hashtagText != ""){
                            hashTagsArray.add(hashtagText)
                            recyclerviewHashTags.adapter!!.notifyItemInserted(recyclerviewHashTags.adapter!!.itemCount)
                        }

                        return true
                    }
                }
                return false
            }
        })

        //저장 경로
        recyclerviewStoreRoute.adapter = StoreRouteAdapter()
        recyclerviewStoreRoute.layoutManager = GridLayoutManager(this, 3)


    }

    private fun getLinkUrl(){

        val intent = intent
        val action = intent.action
        val type = intent.type

        var sharedText : String?
        var url : String?

        if (Intent.ACTION_SEND == action && type != null) {
            if ("text/plain" == type) {
                sharedText = intent.getStringExtra(Intent.EXTRA_TEXT)  // 가져온 인텐트의 텍스트 정보
                url = Util().extractUrlFromText(sharedText)
                Dlog.d(url)

                tvSharedUrl.text = url

                getMetaDataFromUrl(url)

            }
        }
    }

    private fun getMetaDataFromUrl(url: String?) {

        var async = object : AsyncTask<Void,Void,org.jsoup.nodes.Document>(){
            override fun doInBackground(vararg params: Void?): org.jsoup.nodes.Document {
                val doc = Jsoup.connect(url).get()
                Dlog.d(doc.toString())
                return doc
            }

            override fun onPostExecute(result: org.jsoup.nodes.Document?) {
                super.onPostExecute(result)

                val title = result!!.select("meta[property=og:title]").first().attr("content")
                val description = result.select("meta[property=og:description]")[0].attr("content")
                val imageUrl = result.select("meta[property=og:image]")[0].attr("content")

                Dlog.e("title : $title")
                Dlog.e("description : $description")
                Dlog.e("imageUrl : $imageUrl")

                etSharedTitle.setText(title)
            }

        }

        async.execute()

        /*
                var thread = Thread{
                    run {
                            //핸들러 쓰면 죽고 핸들러 안쓰면 안죽는 이유 ㅠㅠ
                            val doc = Jsoup.connect(url).get()
                            Dlog.d(doc.toString())

                            val title = doc.select("meta[property=og:title]").first().attr("content")
                            val description = doc.select("meta[property=og:description]")[0].attr("content")
                            val imageUrl = doc.select("meta[property=og:image]")[0].attr("content")

                            Dlog.e("title : $title")
                            Dlog.e("description : $description")
                            Dlog.e("imageUrl : $imageUrl")
                    }
                }
                thread.start()
         */

    }
}