package com.mashup.molink.detail.adapter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.mashup.molink.R
import com.mashup.molink.data.model.Folder
import com.mashup.molink.utils.FolderDiffCallback

class DetailFolderAdapter : RecyclerView.Adapter<DetailFolderAdapter.DetailFolderViewHolder>() {

    private var items: MutableList<Folder> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        DetailFolderViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.item_detail_folder,
                parent,
                false
            )
        )

    override fun getItemCount() = items.size

    override fun onBindViewHolder(holder: DetailFolderViewHolder, position: Int) {
        items[position].let { item ->
            with(holder) {

                try {
                    ivFolderHead.setColorFilter(Color.parseColor(item.color))
                } catch (e: Exception) {
                    e.printStackTrace()
                }

                tvTitle.text = item.name
            }
        }
    }

    fun getItem(position: Int): Folder? {

        if (position < items.size) {
            return items[position]
        }
        return null
    }

    fun getPosition(id: Int): Int {

        for((index, value) in items.withIndex()) {
            if(value.id == id) {
                return index
            }
        }
        return 0
    }

    fun updateListItems(newItems: MutableList<Folder>) {

        val diffCallback = FolderDiffCallback(items, newItems)
        val diffResult = DiffUtil.calculateDiff(diffCallback)

        items.clear()
        items.addAll(newItems)

        diffResult.dispatchUpdatesTo(this)
    }

    fun setItems(items: MutableList<Folder>) {
        this.items = items
        notifyDataSetChanged()
    }

    class DetailFolderViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val ivFolderHead: ImageView = itemView.findViewById(R.id.ivItemDetailFolderHead)
        val tvTitle: TextView = itemView.findViewById(R.id.tvItemDetailFolderTitle)
    }
}