package com.example.cloud_based_recyclable_household_waste_classification.ui.detail

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
import com.example.cloud_based_recyclable_household_waste_classification.data.remote.response.ArticlesItem

class ArticleAdapter (private val listArticles: List<ArticlesItem>) : RecyclerView.Adapter<ArticleAdapter.ListViewHolder>() {
    private lateinit var onItemClickCallback: OnItemClickCallback

    interface OnItemClickCallback {
        fun onItemClicked(data: ArticlesItem)
    }

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    class ListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imgNews: ImageView = itemView.findViewById(R.id.ivArticleImage)
        val tvArticleTitle: TextView = itemView.findViewById(R.id.tvArticleTitle)
        val tvArticleDesc: TextView = itemView.findViewById(R.id.tvArticleDesc)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.article_item, parent, false)
        return ListViewHolder(view)
    }

    override fun getItemCount(): Int  = listArticles.size

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val titleMaxLength = 60
        val descMaxLength = 100
        val article = listArticles[position]

        val title = article.title?:"No Title"
        val description = article.description?:"No Description"

        holder.tvArticleTitle.text = if (title.length > titleMaxLength) {
            "${title.substring(0, titleMaxLength)}..."
        } else {
            title
        }

        holder.tvArticleDesc.text = if (description.length > descMaxLength) {
            "${description.substring(0, descMaxLength)}..."
        } else {
            description
        }

        Glide.with(holder.itemView.context)
            .load(article.image?:"")
            .into(holder.imgNews)

        holder.itemView.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = Uri.parse(article.url)
            holder.itemView.context.startActivity(intent)
        }
    }
}


