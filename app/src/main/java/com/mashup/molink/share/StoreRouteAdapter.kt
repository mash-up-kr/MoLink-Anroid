package com.mashup.molink.share

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.recyclerview.widget.RecyclerView
import com.mashup.molink.R

//다시
class StoreRouteAdapter: RecyclerView.Adapter<RecyclerView.ViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        var view = LayoutInflater.from(parent!!.context).inflate(R.layout.item_route,parent,false)

        return StoreRouteViewHolder(view)
    }

    class StoreRouteViewHolder(view: View?) : RecyclerView.ViewHolder(view!!) {

        //아..스피너 어케해야하지..
        var routesArray = arrayOf("백엔드", "안드")
        var backEndArray = arrayOf("스프링", "장고", "노드")
        var androidArray = arrayOf("코틀린", "자바")

        var spinner : Spinner? = null

        init {
            spinner = view!!.findViewById(R.id.spinnerRoute)
            spinner!!.adapter = ArrayAdapter<String>(view.context, android.R.layout.simple_spinner_dropdown_item, routesArray)

        }
    }

    override fun getItemCount(): Int {
        return 1
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        var view = holder as StoreRouteViewHolder

        //view.spinner!!.adapter =

    }

}