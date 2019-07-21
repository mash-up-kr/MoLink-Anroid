package com.mashup.molink.detail.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.mashup.molink.R
import com.mashup.molink.data.model.Folder
import com.mashup.molink.utils.FolderDiffCallback

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
                tvFolderTitle.text = item.name

                //전체 버튼
                itemView.setOnClickListener {
                    if(position > 0) {
                        listener?.onItemClick(items[position-1].id, item.id)
                    } else {
                        listener?.onItemClickRoot(item.id)
                    }
                }
            }
        }
    }

    fun updateListItems(newItems: MutableList<Folder>) {

        val diffCallback = FolderDiffCallback(items, newItems)
        val diffResult = DiffUtil.calculateDiff(diffCallback)

        items.clear()
        items.addAll(newItems)

        diffResult.dispatchUpdatesTo(this)
    }

    fun addItem(item: Folder) {
        items.add(item)
        notifyItemInserted(items.size - 1)
    }

    fun deleteItem(id: Int) {

        for(i in items.size - 1 downTo 0) {

            val item = items[i]

            items.removeAt(i)
            notifyItemRemoved(i)

            if(item.id == id) break
        }
    }

    fun setItemClickListener(listener: ItemClickListener?) {
        this.listener = listener
    }

    class NavFolderViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val tvFolderTitle: TextView = itemView.findViewById(R.id.tvItemNavFolderTitle)
    }

    interface ItemClickListener {

        fun onItemClick(beforeFolderId: Int, folderId: Int)

        fun onItemClickRoot(folderId: Int)
    }
}