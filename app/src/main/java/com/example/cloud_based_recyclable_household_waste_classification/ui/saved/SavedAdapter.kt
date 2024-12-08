package com.example.cloud_based_recyclable_household_waste_classification.ui.saved

import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.cloud_based_recyclable_household_waste_classification.R
import com.example.cloud_based_recyclable_household_waste_classification.data.remote.response.ListStoryItem

import com.example.cloud_based_recyclable_household_waste_classification.ui.detail.DetailActivity

class SavedAdapter (private val listSavedItem: List<ListStoryItem>) : RecyclerView.Adapter<SavedAdapter.ListViewHolder>() {
    private lateinit var onItemClickCallback: OnItemClickCallback

    interface OnItemClickCallback {
        fun onItemClicked(data: ListStoryItem)
    }

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    class ListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imgItem: ImageView = itemView.findViewById(R.id.item_image)
        val tvTitle: TextView = itemView.findViewById(R.id.textview_item_title)
        val tvSubtitle: TextView = itemView.findViewById(R.id.textview_item_subtitle)
        val btnDetail: TextView = itemView.findViewById(R.id.btnDetail)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.saved_item, parent, false)
        return ListViewHolder(view)
    }

    override fun getItemCount(): Int  = listSavedItem.size

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val item = listSavedItem[position]
        holder.tvTitle.text = item.className
        holder.tvSubtitle.text = String.format("%.2f%%", item.probability * 100)

        // Load image using Glide
        Glide.with(holder.itemView.context)
            .load(item.imageUrl)
            .into(holder.imgItem)


        holder.btnDetail.setOnClickListener{
            var intent = Intent(holder.itemView.context, DetailActivity::class.java)
            intent.putExtra(DetailActivity.KEY_PROB, item.probability)
            intent.putExtra(DetailActivity.KEY_CLASSNAME, item.className)
            intent.putExtra(DetailActivity.KEY_URL, item.imageUrl)
            intent.putExtra(DetailActivity.KEY_SOURCE, DetailActivity.SOURCE_SAVED_ADAPTER)
            intent.putExtra(DetailActivity.KEY_ACTIVE_SAVED, true)
            intent.putExtra(DetailActivity.KEY_ITEM_ID, item.id)
            holder.itemView.context.startActivity(intent)
        }

    }


}