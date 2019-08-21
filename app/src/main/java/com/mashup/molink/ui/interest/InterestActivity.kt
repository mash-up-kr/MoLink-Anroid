package com.mashup.molink.ui.interest

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.GridLayoutManager
import com.mashup.molink.R
import com.mashup.molink.ui.main.MainActivity
import com.mashup.molink.utils.Dlog
import com.mashup.molink.utils.PrefUtil
import kotlinx.android.synthetic.main.activity_interest.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class InterestActivity : AppCompatActivity(), InterestAdapter.OnItemClickListener {


    private val adapter = InterestAdapter()

    var check_sum: Int = 0

    override fun onClick(interest: Data) {

        for(i in adapter.items){
            if(i.check){
                check_sum++
                break
            }
        }

        if(check_sum>0){
            btnActivityInterestOk.setBackgroundColor(ContextCompat.getColor(applicationContext, R.color.black))
            btnActivityInterestOk.isEnabled=true

            check_sum=0

        }else{
            btnActivityInterestOk.setBackgroundColor(ContextCompat.getColor(applicationContext, R.color.cardview_shadow_start_color))
            btnActivityInterestOk.isEnabled=false
        }

    }

    var retrofit = Retrofit.Builder()
        .baseUrl("http://ec2-52-79-252-3.ap-northeast-2.compute.amazonaws.com:8080")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    var service: InterestAPI = retrofit.create(InterestAPI::class.java!!)
    var repos = service.getInterest()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_interest)

        rvActivityInterest.layoutManager = GridLayoutManager(this, 3)
        rvActivityInterest.adapter = adapter



        adapter.setClickListener(this)


        btnActivityInterestOk.setOnClickListener {

            val list = ArrayList<String>()
            Dlog.e(adapter.getItem().size.toString())

            for((index, value) in adapter.items.withIndex()){
                if(value.check){
                    list.add(value.name!!)
                    Dlog.e(value.name)
                }
            }

            PrefUtil.put(PrefUtil.PREF_IS_FIRST_RUN, true)

            val intent = Intent(this@InterestActivity, MainActivity::class.java)
            intent.putExtra(MainActivity.KEY_INTERESTS, list)
            startActivity(intent)
            finish()
        }

        loadData()

    }

    private fun loadData() {

        repos.enqueue(object : Callback<Model>{
            override fun onResponse(call: Call<Model>, response: Response<Model>) {
                if(response.isSuccessful) {
                    val model = response.body()
                    Dlog.d("model : $model")
                    if(model != null) {
                        val items = model.data
                        adapter.setItem(items as ArrayList<Data>)
                    }
                } else {
                    val statusCode = response.code()
                    Dlog.d("fail statusCode : $statusCode")
                }
            }

            override fun onFailure(call: Call<Model>, t: Throwable) {
                Dlog.d("t : ${t.message}")
            }



        })
    }

}