package com.example.cloud_based_recyclable_household_waste_classification.data.remote.response

import com.google.gson.annotations.SerializedName

data class ArticleResponse(

	@field:SerializedName("totalArticles")
	val totalArticles: Int,

	@field:SerializedName("articles")
	val articles: List<ArticlesItem>
)

data class Source(

	@field:SerializedName("name")
	val name: String,

	@field:SerializedName("url")
	val url: String
)

data class ArticlesItem(

	@field:SerializedName("image")
	val image: String,

	@field:SerializedName("publishedAt")
	val publishedAt: String,

	@field:SerializedName("description")
	val description: String,

	@field:SerializedName("source")
	val source: Source,

	@field:SerializedName("title")
	val title: String,

	@field:SerializedName("content")
	val content: String,

	@field:SerializedName("url")
	val url: String
)
