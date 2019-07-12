package com.mashup.molink.interest

import android.graphics.Color
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.mashup.molink.R
import android.view.LayoutInflater
import android.view.ViewGroup
import com.bumptech.glide.Glide
import android.widget.TextView
import android.view.View
import android.widget.Button
import androidx.core.content.ContextCompat
import com.mashup.molink.utils.Dlog
import kotlinx.android.synthetic.main.item_interest.view.*

class InterestAdapter: RecyclerView.Adapter<InterestAdapter.ViewHolder>(){

    var items = ArrayList<Data>()
    lateinit var listener: OnItemClickListener

    override fun onCreateViewHolder(parent: ViewGroup, i: Int): InterestAdapter.ViewHolder {

        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_interest, parent, false)

        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(viewHolder: InterestAdapter.ViewHolder, position: Int) {

        val item = items.get(position)
        viewHolder.tvName.text=item.name

        Glide.with(viewHolder.itemView.context)
            .load(item.imgUrl)
            .into(viewHolder.ivInterest)

        viewHolder.check=item.check
        viewHolder.itemView.setOnClickListener({ view ->

            if (viewHolder.itemView.ivInterestCheck.visibility == View.VISIBLE) {
                viewHolder.itemView.ivInterestCheck.visibility = View.INVISIBLE
                item.check = false
            } else {
                viewHolder.itemView.ivInterestCheck.visibility = View.VISIBLE
                item.check = true
            }
            listener.onClick(item)

            Dlog.d("dapter")

        })

       // viewHolder.bind(item)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    fun setItem(items: ArrayList<Data>) {
        this.items = items
        notifyDataSetChanged()
    }

    fun getItem(): ArrayList<Data>{
        return items
    }

    fun setClickListener(listener: OnItemClickListener){
        this.listener = listener
    }

    interface OnItemClickListener {
        fun onClick(interest: Data)
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val tvName = itemView.tvItemName //관심사 이름
        var ivInterest = itemView.ivInterest //관심사 이미지
        val ivInterestCheck = itemView.ivInterestCheck //관심사 체크버튼
        var check = itemView.ivInterestCheck.isShown //관심사 체크 여부 true면 검은색 박스

//        fun bind(item: Interest){
//            itemView.setOnClickListener {
//
//            }
//        }

    }





}