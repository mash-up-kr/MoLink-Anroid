package com.mashup.molink.detail

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.mashup.molink.R
import com.mashup.molink.model.ItemLink
import com.mashup.molink.utils.Dlog

class LinkAdapter : RecyclerView.Adapter<LinkAdapter.BaseViewHolder>() {

    private var items: MutableList<ItemLink> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {

        when (viewType) {

            1 -> return SubFolderViewHolder.newInstance(parent)

            2 -> return LinkViewHolder.newInstance(parent)
        }

        return SubFolderViewHolder.newInstance(parent)
    }

    override fun getItemCount() = items.size

    override fun getItemViewType(position: Int): Int = items[position].viewType

    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
        holder.onBindView(items[position])
    }

    fun setItems(items: MutableList<ItemLink>) {
        Dlog.d("setItems items : ${items.size}")
        this.items = items
        notifyDataSetChanged()
    }


    abstract class BaseViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        abstract fun onBindView(item: ItemLink)
    }

    class SubFolderViewHolder(
        mItemView: View
    ) : BaseViewHolder(mItemView) {

        private val ivFolderHead: ImageView = mItemView.findViewById(R.id.ivItemSubFolderHead)
        private val tvFolderTitle: TextView = mItemView.findViewById(R.id.tvItemSubFolderTitle)

        override fun onBindView(item: ItemLink) {

            try {
                ivFolderHead.setColorFilter(Color.parseColor(item.color))
            } catch (e: Exception) {
                e.printStackTrace()
            }

            tvFolderTitle.text = item.title
        }

        companion object {

            fun newInstance(parent: ViewGroup): BaseViewHolder {

                val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_sub_folder, parent, false)

                return SubFolderViewHolder(itemView)
            }
        }
    }

    class LinkViewHolder(
        itemView: View
    ) : BaseViewHolder(itemView) {

        private val tvLinkTitle: TextView = itemView.findViewById(R.id.tvItemLinkTitle)
        private val tvLinkUrl: TextView = itemView.findViewById(R.id.tvItemLinkUrl)
        private val ivLinkModify: ImageView = itemView.findViewById(R.id.ivItemLinkModify)

        override fun onBindView(item: ItemLink) {

            tvLinkTitle.text = item.title
            tvLinkUrl.text = item.color

        }

        companion object {

            fun newInstance(parent: ViewGroup): BaseViewHolder {

                val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_link, parent, false)

                return LinkViewHolder(itemView)
            }
        }
    }


}