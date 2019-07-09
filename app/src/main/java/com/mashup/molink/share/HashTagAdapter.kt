package com.mashup.molink.share

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.mashup.molink.R

class HashTagAdapter(var hashTagsArray : ArrayList<String>) : RecyclerView.Adapter<RecyclerView.ViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        var view = LayoutInflater.from(parent!!.context).inflate(R.layout.item_hashtag,parent,false)

        return HashTagViewHolder(view)
    }

    class HashTagViewHolder(view: View?) : RecyclerView.ViewHolder(view!!) {

        var textView : TextView? = null

        init {
            textView = view!!.findViewById(R.id.tvItemHashtag)

        }
    }

    override fun getItemCount(): Int {
        return hashTagsArray.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        var view = holder as HashTagViewHolder
        view!!.textView!!.text = hashTagsArray[position]
    }


}