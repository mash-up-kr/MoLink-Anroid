package com.mashup.molink.detail.adapter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.mashup.molink.R
import com.mashup.molink.data.model.Folder
import com.mashup.molink.data.model.Link
import com.mashup.molink.detail.adapter.model.LinkAndFolderModel

class LinkAndFolderAdapter : RecyclerView.Adapter<LinkAndFolderAdapter.BaseViewHolder>() {

    private var items: MutableList<LinkAndFolderModel> = mutableListOf()

    private var listener: ItemClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {

        when (viewType) {

            1 -> return ChildFolderViewHolder.newInstance(
                parent,
                listener
            )

            2 -> return LinkViewHolder.newInstance(
                parent,
                listener
            )
        }

        return ChildFolderViewHolder.newInstance(
            parent,
            listener
        )
    }

    override fun getItemCount() = items.size

    override fun getItemViewType(position: Int): Int = items[position].viewType

    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
        holder.onBindView(items[position])
    }

    fun setItems(items: MutableList<LinkAndFolderModel>) {
        this.items = items
        notifyDataSetChanged()
    }
    
    fun addFolder(item: Folder) {
        val temp = LinkAndFolderModel(
            fid = item.id,
            title = item.name,
            color = item.color,
            viewType = 1
        )

        //폴더의 마지막 위치를 가져옴
        var index = 0
        for(i in 0 until items.size) {
            if(items[i].viewType == 2) {
                index = i
                break
            }
        }
        items.add(index, temp)
        notifyItemInserted(index)
    }

    fun modifyLink(item: Link) {
        for((index, value) in items.filter { it.viewType == 2 }.withIndex()) {
            if(value.lid == item.id) {
                val temp = items[index].copy(title = item.name, url = item.url)
                items[index] = temp
                notifyItemChanged(index)
                break
            }
        }
    }

    fun deleteLink(item: Link) {
        for((index, value) in items.filter { it.viewType == 2 }.withIndex()) {
            if(value.lid == item.id) {
                items.removeAt(index)
                notifyItemRemoved(index)
                break
            }
        }
    }

    /**
     * ClickListener
     */
    fun setItemClickListener(listener: ItemClickListener) {
            this.listener = listener
    }

    interface ItemClickListener {

        fun onItemFolderClick(item: LinkAndFolderModel)

        fun onItemLinkClick(item: LinkAndFolderModel)

        fun onItemLinkModifyClick(item: LinkAndFolderModel)
    }

    /**
     * ViewHolder
     */
    abstract class BaseViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        abstract fun onBindView(linkAndFolderModel: LinkAndFolderModel)
    }

    class ChildFolderViewHolder(
        mItemView: View,
        private val listener: ItemClickListener?
    ) : BaseViewHolder(mItemView) {

        private val ivFolderHead: ImageView = mItemView.findViewById(R.id.ivItemChildFolderHead)
        private val tvFolderTitle: TextView = mItemView.findViewById(R.id.tvItemChildFolderTitle)

        override fun onBindView(linkAndFolderModel: LinkAndFolderModel) {

            try {
                ivFolderHead.setColorFilter(Color.parseColor(linkAndFolderModel.color))
            } catch (e: Exception) {
                e.printStackTrace()
            }

            tvFolderTitle.text = linkAndFolderModel.title

            itemView.setOnClickListener {
                listener?.onItemFolderClick(linkAndFolderModel)
            }
        }

        companion object {

            fun newInstance(parent: ViewGroup, listener: ItemClickListener?): BaseViewHolder {

                val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_child_folder, parent, false)

                return ChildFolderViewHolder(itemView, listener)
            }
        }
    }

    class LinkViewHolder(
        itemView: View,
        private val listener: ItemClickListener?
    ) : BaseViewHolder(itemView) {

        private val tvLinkTitle: TextView = itemView.findViewById(R.id.tvItemLinkTitle)
        private val tvLinkUrl: TextView = itemView.findViewById(R.id.tvItemLinkUrl)
        private val ivLinkModify: ImageView = itemView.findViewById(R.id.ivItemLinkModify)

        override fun onBindView(linkAndFolderModel: LinkAndFolderModel) {

            tvLinkTitle.text = linkAndFolderModel.title
            tvLinkUrl.text = linkAndFolderModel.url

            itemView.setOnClickListener {
                listener?.onItemLinkClick(linkAndFolderModel)
            }

            ivLinkModify.setOnClickListener {
                listener?.onItemLinkModifyClick(linkAndFolderModel)
            }

        }

        companion object {

            fun newInstance(parent: ViewGroup, listener: ItemClickListener?): BaseViewHolder {

                val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_link, parent, false)

                return LinkViewHolder(itemView, listener)
            }
        }
    }

}