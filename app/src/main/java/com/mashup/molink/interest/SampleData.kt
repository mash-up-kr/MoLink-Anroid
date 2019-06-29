package com.mashup.molink.interest

import android.content.Context
import androidx.core.content.ContextCompat
import com.mashup.molink.R

class SampleData {

    var items = ArrayList<Interest>()

    fun getItem(context: Context): ArrayList<Interest> {

        val interest1 = Interest("디자인", false, ContextCompat.getDrawable(context, R.drawable.img_design))
        val interest2 = Interest("스포츠", false, ContextCompat.getDrawable(context, R.drawable.img_sports))
        val interest3 = Interest("코스메틱", false, ContextCompat.getDrawable(context, R.drawable.img_cosmetic))
        val interest4 = Interest("푸드", false, ContextCompat.getDrawable(context, R.drawable.img_food))
        val interest5 = Interest("꽃", false, ContextCompat.getDrawable(context, R.drawable.img_flower))
        val interest6 = Interest("영화", false, ContextCompat.getDrawable(context, R.drawable.img_movie))
        val interest7 = Interest("독서", false, ContextCompat.getDrawable(context, R.drawable.img_book))
        val interest8 = Interest("바다", false, ContextCompat.getDrawable(context, R.drawable.img_sea))
        val interest9 = Interest("공부", false, ContextCompat.getDrawable(context, R.drawable.img_study))
        val interest10 = Interest("디자인", false, ContextCompat.getDrawable(context, R.drawable.img_design))
        val interest11 = Interest("디자인", false, ContextCompat.getDrawable(context, R.drawable.img_design))
        val interest12 = Interest("디자인", false, ContextCompat.getDrawable(context, R.drawable.img_design))
        val interest13 = Interest("디자인", false, ContextCompat.getDrawable(context, R.drawable.img_design))
        val interest14 = Interest("디자인", false, ContextCompat.getDrawable(context, R.drawable.img_design))
        val interest15 = Interest("디자인", false, ContextCompat.getDrawable(context, R.drawable.img_design))



        items.add(interest1)
        items.add(interest2)
        items.add(interest3)

        items.add(interest4)
        items.add(interest5)
        items.add(interest6)

        items.add(interest7)
        items.add(interest8)
        items.add(interest9)

        items.add(interest10)
        items.add(interest11)
        items.add(interest12)

        items.add(interest13)
        items.add(interest14)
        items.add(interest15)

        return items
    }
}