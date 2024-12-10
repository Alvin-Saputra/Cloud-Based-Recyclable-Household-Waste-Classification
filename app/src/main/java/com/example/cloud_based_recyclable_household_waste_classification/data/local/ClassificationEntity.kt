package com.example.cloud_based_recyclable_household_waste_classification.data.local

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "classification")
data class ClassificationEntity (

    @field:PrimaryKey(autoGenerate = true)
    @field:ColumnInfo(name = "id")
    var id: Int = 0,

    @field:ColumnInfo(name = "className")
    var className: String? = null,

    @field:ColumnInfo(name = "probability")
    var probability: Double? = null,

    @field:ColumnInfo(name = "imageUrl")
    var imageUrl: String? = null,
)