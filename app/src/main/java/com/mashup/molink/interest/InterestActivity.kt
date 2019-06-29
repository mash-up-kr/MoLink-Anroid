package com.mashup.molink.interest

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import com.mashup.molink.R
import com.mashup.molink.main.MainActivity
import com.mashup.molink.utils.Dlog
import kotlinx.android.synthetic.main.activity_interest.*

class InterestActivity : AppCompatActivity() {

    private val adapter = InterestAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_interest)

        rvActivityInterest.layoutManager = GridLayoutManager(this, 3)
        rvActivityInterest.adapter = adapter

        adapter.setItem(SampleData().getItem(this))

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
        }

    }
}