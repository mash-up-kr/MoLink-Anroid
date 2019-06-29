package com.mashup.molink.interest

import android.graphics.Movie



class SampleData {

    var items = ArrayList<Interest>()

    fun getItem(): ArrayList<Interest> {

        val interest1 = Interest("뷰티", false)
        val interest2 = Interest("음악", false)
        val interest3 = Interest("개발", false)
        val interest4 = Interest("뷰티1", false)
        val interest5 = Interest("음악1", false)
        val interest6 = Interest("개발1", false)
        val interest7 = Interest("뷰티2", false)
        val interest8 = Interest("음악2", false)
        val interest9 = Interest("개발2", false)
        val interest10 = Interest("뷰티3", false)
        val interest11 = Interest("음악3", false)
        val interest12 = Interest("개발3", false)
        val interest13 = Interest("뷰티4", false)
        val interest14 = Interest("음악4", false)
        val interest15 = Interest("개발4", false)




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