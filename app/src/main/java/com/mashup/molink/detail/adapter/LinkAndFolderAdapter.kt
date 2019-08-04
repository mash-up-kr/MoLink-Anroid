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
import com.mashup.molink.utils.Dlog

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
        this.items.clear()
        this.items.addAll(items)
        notifyDataSetChanged()
    }

    fun setFolders(folders: List<Folder>) {

        val newItems = mutableListOf<LinkAndFolderModel>()

        items.filter { it.viewType == 2 }
            .forEach { newItems.add(it) }

        Dlog.d("folders newItems : $newItems")
        items = newItems

        for(folder in folders) {
            val temp =
                LinkAndFolderModel(
                    fid = folder.id,
                    title = folder.name,
                    color = folder.color,
                    viewType = 1
                )
            items.add(temp)
            items.sortBy { it.viewType }
        }

        notifyDataSetChanged()
    }

    fun setLinks(links: List<Link>) {

        val newItems = mutableListOf<LinkAndFolderModel>()

        items.filter { it.viewType == 1 }
            .forEach { newItems.add(it) }

        Dlog.d("setLinks newItems : $newItems")
        items = newItems

        for(link in links) {
            val temp =
                LinkAndFolderModel(
                    lid = link.id,
                    title = link.name,
                    url = link.url,
                    viewType = 2
                )
            items.add(temp)
            items.sortBy { it.viewType }
        }

        notifyDataSetChanged()
    }

    /**
     * ClickListener
     */
    fun setItemClickListener(listener: ItemClickListener) {
            this.listener = listener
    }

    interface ItemClickListener {

        fun onItemFolderClick(item: LinkAndFolderModel)

        fun onItemFolderLongClick(item: LinkAndFolderModel)

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

            itemView.setOnLongClickListener {
                listener?.onItemFolderLongClick(linkAndFolderModel)
                false
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