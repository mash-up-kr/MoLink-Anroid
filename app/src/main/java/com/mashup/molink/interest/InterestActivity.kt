package com.mashup.molink.interest

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.GridLayoutManager
import com.jakewharton.rxbinding2.view.enabled
import com.mashup.molink.R
import com.mashup.molink.main.MainActivity
import com.mashup.molink.utils.Dlog
import kotlinx.android.synthetic.main.activity_interest.*
import retrofit2.Retrofit


class InterestActivity : AppCompatActivity(), InterestAdapter.OnItemClickListener {

    private val adapter = InterestAdapter()

    var check_sum: Int = 0

    override fun onClick(interest: Interest) {

        for(i in adapter.items){
            if(i.check){
                check_sum++
                break
            }
        }

        var prefs = getSharedPreferences("Pref", Context.MODE_PRIVATE)

        prefs.edit().putBoolean("isFirstRun",false).apply()

        if(check_sum>0){
            btnActivityInterestOk.setBackgroundColor(ContextCompat.getColor(applicationContext, R.color.black))
            btnActivityInterestOk.isEnabled=true

            check_sum=0

        }else{
            btnActivityInterestOk.setBackgroundColor(ContextCompat.getColor(applicationContext, R.color.cardview_shadow_start_color))
            btnActivityInterestOk.isEnabled=false
        }

    }
//
//    var retrofit = Retrofit.Builder()
//        .baseUrl("https://api.github.com/")
//        .build()
//
//    var service: GithubService = retrofit.create(GithubService::class.java!!)
//    var repos = service.listRepos("octocat")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_interest)

        rvActivityInterest.layoutManager = GridLayoutManager(this, 3)
        rvActivityInterest.adapter = adapter

        adapter.setItem(SampleData().getItem(this))

        adapter.setClickListener(this)

        btnActivityInterestOk.setOnClickListener {
            val list = ArrayList<String>()
            Dlog.e(adapter.getItem().size.toString())

            for((index, value) in adapter.items.withIndex()){
                Dlog.d("$index : ${value.check}")
                if(value.check){
                    list.add(value.name)
                    Dlog.e(value.name)
                }
            }

            var intent = Intent(this@InterestActivity, MainActivity::class.java)
            intent.putExtra("KEY_INTERESTS", list)
            startActivity(intent)
            finish()
        }

    }

}