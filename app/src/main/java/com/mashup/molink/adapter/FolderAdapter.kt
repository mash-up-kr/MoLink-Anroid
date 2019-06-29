package com.mashup.molink.adapter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.mashup.molink.R
import com.mashup.molink.model.Folder

class FolderAdapter : RecyclerView.Adapter<FolderAdapter.FolderViewHolder>() {

    private var items: MutableList<Folder> = mutableListOf()

    private var listener: ItemClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        FolderViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.item_folder,
                parent,
                false
            )
        )

    override fun getItemCount() = items.size

    override fun onBindViewHolder(holder: FolderViewHolder, position: Int) {
        items[position].let { item ->
            with(holder) {

                try {
                    ivFolder.setBackgroundColor(Color.parseColor(item.color))
                } catch (e: Exception) {
                    e.printStackTrace()
                }

                tvTitle.text = item.title

                //수정 버튼
                rlModify.setOnClickListener {
                    listener?.onItemClickModify(item)
                }

                //전체 버튼
                itemView.setOnClickListener {
                    listener?.onItemClick(item)
                }
            }
        }
    }

    fun setItems(items: MutableList<Folder>) {
        this.items = items
        notifyDataSetChanged()
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

    fun modifyItem(item: Folder) {

        for((index, value) in items.withIndex()) {
            if(value.fid == item.fid) {
                items[index] = item
                notifyItemChanged(index)
                break
            }
        }
    }

    fun setItemClickListener(listener: ItemClickListener?) {
        this.listener = listener
    }

    class FolderViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val ivFolder: ImageView = itemView.findViewById(R.id.ivItemFolder)
        val tvTitle: TextView = itemView.findViewById(R.id.tvItemFolderTitle)
        val rlModify: RelativeLayout = itemView.findViewById(R.id.rlItemFolderModify)
    }

    interface ItemClickListener {

        fun onItemClick(folder: Folder)

        fun onItemClickModify(folder: Folder)
    }
}