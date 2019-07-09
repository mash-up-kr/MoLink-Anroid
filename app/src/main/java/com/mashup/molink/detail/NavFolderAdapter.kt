package com.mashup.molink.detail

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.mashup.molink.R
import com.mashup.molink.model.Folder

class NavFolderAdapter : RecyclerView.Adapter<NavFolderAdapter.NavFolderViewHolder>() {

    private var items: MutableList<Folder> = mutableListOf()

    private var listener: ItemClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        NavFolderViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.item_nav_folder,
                parent,
                false
            )
        )

    override fun getItemCount() = items.size

    override fun onBindViewHolder(holder: NavFolderViewHolder, position: Int) {
        items[position].let { item ->
            with(holder) {
                tvFolderTitle.text = item.title

                //전체 버튼
                itemView.setOnClickListener {
                    listener?.onItemClick(item)
                }
            }
        }
    }

    fun addItem(item: Folder) {
        items.add(item)
        notifyItemInserted(items.size - 1)
    }

    fun deleteItem(fid: Int) {

        for((index, value) in items.withIndex()) {
            if(value.fid == fid) {
                items.removeAt(index)
                notifyItemRemoved(index)
                break
            }
        }
    }

    fun setItemClickListener(listener: ItemClickListener?) {
        this.listener = listener
    }

    class NavFolderViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val tvFolderTitle: TextView = itemView.findViewById(R.id.tvItemNavFolderTitle)
    }

    interface ItemClickListener {

        fun onItemClick(folder: Folder)
    }
}