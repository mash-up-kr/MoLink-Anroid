package com.mashup.molink.interest

import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.mashup.molink.R
import android.view.LayoutInflater
import android.view.ViewGroup
import com.bumptech.glide.Glide
import android.widget.TextView
import android.view.View
import androidx.core.content.ContextCompat
import kotlinx.android.synthetic.main.item_interest.view.*

class InterestAdapter: RecyclerView.Adapter<InterestAdapter.ViewHolder>(){

    var items = ArrayList<Interest>()

    override fun onCreateViewHolder(parent: ViewGroup, i: Int): InterestAdapter.ViewHolder {

        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_interest, parent, false)

        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(viewHolder: InterestAdapter.ViewHolder, position: Int) {

        val item = items.get(position)

        viewHolder.tvName.text=item.name
        viewHolder.ivInterest.setImageDrawable(item.src)
        viewHolder.check=item.check

        viewHolder.bind(item)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    fun setItem(items: ArrayList<Interest>) {
        this.items = items
    }

    fun getItem(): ArrayList<Interest>{
        return items
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val tvName = itemView.tvItemName //관심사 이름
        var ivInterest = itemView.ivInterest //관심사 이미지
        val ivInterestCheck = itemView.ivInterestCheck //관심사 체크버튼
        var check = itemView.ivInterestCheck.isShown //관심사 체크 여부

        fun bind(item: Interest){
            itemView.setOnClickListener {
                if(ivInterestCheck.visibility==View.VISIBLE){
                    ivInterestCheck.visibility=View.INVISIBLE
                    item.check=false
                }else{
                    ivInterestCheck.visibility=View.VISIBLE
                    item.check=true
                }
            }
        }

    }





}