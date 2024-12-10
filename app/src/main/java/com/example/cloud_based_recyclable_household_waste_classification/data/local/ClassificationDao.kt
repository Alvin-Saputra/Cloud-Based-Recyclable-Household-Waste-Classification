package com.example.cloud_based_recyclable_household_waste_classification.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface ClassificationDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(classificationItems: List<ClassificationEntity>)

    @Query("DELETE FROM classification")
    suspend fun deleteAll()

    @Query("SELECT * FROM classification")
    suspend fun getAll(): List<ClassificationEntity>
}