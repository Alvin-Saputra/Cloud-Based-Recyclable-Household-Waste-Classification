package com.example.cloud_based_recyclable_household_waste_classification.data.remote.response

import com.google.gson.annotations.SerializedName

data class ArticleResponse(

    @field:SerializedName("totalResults")
	val totalResults: Int,

    @field:SerializedName("nextPage")
	val nextPage: String,

    @field:SerializedName("results")
	val results: List<ResultsItem>,

    @field:SerializedName("status")
	val status: String
)

data class ResultsItem(

	@field:SerializedName("country")
	val country: List<String>,

	@field:SerializedName("sentiment")
	val sentiment: String,

	@field:SerializedName("pubDateTZ")
	val pubDateTZ: String,

	@field:SerializedName("keywords")
	val keywords: Any,

	@field:SerializedName("link")
	val link: String,

	@field:SerializedName("description")
	val description: String,

	@field:SerializedName("language")
	val language: String,

	@field:SerializedName("title")
	val title: String,

	@field:SerializedName("content")
	val content: String,

	@field:SerializedName("source_url")
	val sourceUrl: String,

	@field:SerializedName("article_id")
	val articleId: String,

	@field:SerializedName("video_url")
	val videoUrl: Any,

	@field:SerializedName("source_priority")
	val sourcePriority: Int,

	@field:SerializedName("source_name")
	val sourceName: String,

	@field:SerializedName("creator")
	val creator: Any,

	@field:SerializedName("sentiment_stats")
	val sentimentStats: String,

	@field:SerializedName("image_url")
	val imageUrl: String,

	@field:SerializedName("ai_region")
	val aiRegion: String,

	@field:SerializedName("duplicate")
	val duplicate: Boolean,

	@field:SerializedName("pubDate")
	val pubDate: String,

	@field:SerializedName("source_icon")
	val sourceIcon: String,

	@field:SerializedName("ai_org")
	val aiOrg: String,

	@field:SerializedName("ai_tag")
	val aiTag: String,

	@field:SerializedName("source_id")
	val sourceId: String,

	@field:SerializedName("category")
	val category: List<String>
)
