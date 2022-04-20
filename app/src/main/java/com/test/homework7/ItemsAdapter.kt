package com.test.homework7

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class ItemsAdapter(private val items: List<JsonItem>) : RecyclerView.Adapter<ItemViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val rootView = LayoutInflater
            .from(parent.context)
            .inflate(R.layout.json_item, parent, false)

        return ItemViewHolder(rootView)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        holder.bind(items[position])
    }
}

class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    private val textIdView: TextView = itemView.findViewById(R.id.id)
    private val textUserIdView: TextView = itemView.findViewById(R.id.userId)
    private val textTitleView: TextView = itemView.findViewById(R.id.title)
    private val textBodyView: TextView = itemView.findViewById(R.id.body)

    fun bind(item: JsonItem) {
        textIdView.text = item.id.toString()
        textUserIdView.text = item.userId.toString()
        textTitleView.text = item.title
        textBodyView.text = item.body
    }
}