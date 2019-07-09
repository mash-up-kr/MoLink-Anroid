package com.mashup.molink.main

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.mashup.molink.R
import com.mashup.molink.model.Folder
import com.mashup.molink.utils.Dlog

class MainFolderAdapter : RecyclerView.Adapter<MainFolderAdapter.FolderViewHolder>() {

    private var items: MutableList<Folder> = mutableListOf()

    private var listener: ItemClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        FolderViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.item_main_folder,
                parent,
                false
            )
        )

    override fun getItemCount() = items.size

    override fun onBindViewHolder(holder: FolderViewHolder, position: Int) {
        items[position].let { item ->
            with(holder) {

                try {
                    ivFolderHead.setColorFilter(Color.parseColor(item.color))
                } catch (e: Exception) {
                    e.printStackTrace()
                }

                tvTitle.text = item.title

                //수정 버튼
                llModify.setOnClickListener {
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
        Dlog.d("setItems items : ${items.size}")
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

        val ivFolderHead: ImageView = itemView.findViewById(R.id.ivItemMainFolderHead)
        val tvTitle: TextView = itemView.findViewById(R.id.tvItemMainFolderTitle)
        val llModify: LinearLayout = itemView.findViewById(R.id.llItemMainFolderModify)
    }

    interface ItemClickListener {

        fun onItemClick(folder: Folder)

        fun onItemClickModify(folder: Folder)
    }
}