package com.mashup.molink.detail

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.mashup.molink.R
import com.mashup.molink.model.Folder
import com.mashup.molink.utils.Dlog

class DetailActivity : AppCompatActivity() {

    companion object {

        const val KEY_FOLDER = "folder"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        getData()

    }

    private fun getData() {

        val folder = intent?.getParcelableExtra<Folder>(KEY_FOLDER)
        Dlog.d("folder : $folder")

        if(folder == null) {
            error("folder must be not null")
        }
    }
}