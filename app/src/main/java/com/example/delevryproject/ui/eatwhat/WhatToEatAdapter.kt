package com.example.delevryproject.ui.eatwhat

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.delevryproject.R
import com.example.delevryproject.data.model.WhatToEat
import com.google.android.flexbox.FlexDirection
import com.google.android.flexbox.FlexboxLayoutManager
import com.google.android.flexbox.JustifyContent
import kotlinx.android.synthetic.main.item_layout_tags.view.*
import kotlinx.android.synthetic.main.item_layout_what_to_eat.view.*


class WhatToEatAdapter() :
    ListAdapter<WhatToEat, RecyclerView.ViewHolder>(WhatToEatDiffCallback()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return WhatToEatViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val whatToEatItem = getItem(position)
        (holder as WhatToEatViewHolder).bind(whatToEatItem)
    }

    class WhatToEatViewHolder
    private constructor(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(item: WhatToEat) {
            itemView.tv_title.text = item.title
            Glide.with(itemView.context).load(item.imageUrl).into(itemView.image)
            itemView.recyclerView2.apply {
                val flexBoxLayoutManager = FlexboxLayoutManager(itemView.context)
                flexBoxLayoutManager.flexDirection = FlexDirection.ROW
                flexBoxLayoutManager.justifyContent = JustifyContent.FLEX_START
                layoutManager = flexBoxLayoutManager
                adapter = FlexBoxListAdapter(item.tags.split(","))
            }
        }

        companion object {
            fun from(parent: ViewGroup): WhatToEatViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val layout = layoutInflater.inflate(R.layout.item_layout_what_to_eat, parent, false)
                return WhatToEatViewHolder(layout)
            }
        }
    }

    fun setList(list: List<WhatToEat>) {
        submitList(list)
    }
}
//Item이 추가되거나 삭제되거나 하는 것을 모두 DiffUtil이 알아서 처리 결국 notifyDataSetChanged()를 따로 호출해줄 필요가 없게 되는 것
class WhatToEatDiffCallback : DiffUtil.ItemCallback<WhatToEat>() {
    override fun areItemsTheSame(oldItem: WhatToEat, newItem: WhatToEat): Boolean {
        return oldItem.title == newItem.title
    }

    override fun areContentsTheSame(oldItem: WhatToEat, newItem: WhatToEat): Boolean {
        return oldItem == newItem
    }
}

class FlexBoxListAdapter(val list: List<String>) :
    RecyclerView.Adapter<FlexBoxListAdapter.TagsViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TagsViewHolder {
        return TagsViewHolder.from(parent)
    }

    override fun getItemCount() = list.size

    override fun onBindViewHolder(holder: TagsViewHolder, position: Int) {
        holder.bind(list[position])
    }

    class TagsViewHolder constructor(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(tag: String) {
            itemView.tv_tag.text = "${tag.trim()} >"
        }

        companion object {
            fun from(parent: ViewGroup): TagsViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val layout = layoutInflater.inflate(R.layout.item_layout_tags, parent, false)
                return TagsViewHolder(layout)
            }
        }
    }
}