package com.mashup.molink.ui.main

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.mashup.molink.R
import com.mashup.molink.ui.main.fragment.feed.FeedFragment
import com.mashup.molink.ui.main.fragment.follow.FollowFragment
import com.mashup.molink.ui.main.fragment.main.MainFragment
import com.mashup.molink.utils.Dlog
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private lateinit var mainFragment: MainFragment
    private val feedFragment = FeedFragment.newInstance()
    private val followFragment = FollowFragment.newInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val interest = intent?.getSerializableExtra(MainFragment.KEY_INTERESTS)
        Dlog.d("interest : $interest")
        mainFragment =  MainFragment.newInstance(interest)

        supportFragmentManager
            .beginTransaction()
            .replace(R.id.frameLayout, mainFragment)
            .commitAllowingStateLoss()

        navigationView.setOnNavigationItemSelectedListener { menu ->

            when(menu.itemId) {

                R.id.main -> {
                    supportFragmentManager
                        .beginTransaction()
                        .replace(R.id.frameLayout, mainFragment)
                        .commitAllowingStateLoss()
                }

                R.id.feed -> {
                    supportFragmentManager
                        .beginTransaction()
                        .replace(R.id.frameLayout, feedFragment)
                        .commitAllowingStateLoss()
                }

                R.id.follow -> {
                    supportFragmentManager
                        .beginTransaction()
                        .replace(R.id.frameLayout, followFragment)
                        .commitAllowingStateLoss()
                }
            }

            true
        }
    }
}