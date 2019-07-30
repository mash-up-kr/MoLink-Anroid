package com.mashup.molink.share

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.mashup.molink.R
import com.mashup.molink.data.model.Folder

class SelectFolderAdapter : RecyclerView.Adapter<SelectFolderAdapter.FolderViewHolder>() {

    private var items: MutableList<Folder> = mutableListOf()

    private var listener: ItemClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        FolderViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.item_select_folder,
                parent,
                false
            )
        )

    override fun getItemCount() = items.size

    override fun onBindViewHolder(holder: FolderViewHolder, position: Int) {
        items[position].let { item ->

            holder.tvTitle.text =  item.name

            holder.itemView.setOnClickListener {
                listener?.onItemClick(item)
            }
        }
    }

    fun setItem(items : MutableList<Folder>) {
        this.items = items
        notifyDataSetChanged()
    }

    fun setItemClickListener(listener: ItemClickListener) {
        this.listener = listener
    }

    interface ItemClickListener {

        fun onItemClick(folder: Folder)
    }

    class FolderViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val tvTitle: TextView = itemView.findViewById(R.id.tvItemSelectFolderTitle)
    }

}