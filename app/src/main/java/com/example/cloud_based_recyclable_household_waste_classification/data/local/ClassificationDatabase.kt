package com.example.cloud_based_recyclable_household_waste_classification.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [ClassificationEntity::class], version = 1, exportSchema = false)
abstract class ClassificationDatabase : RoomDatabase() {
    abstract fun classificationDao(): ClassificationDao

    companion object {
        @Volatile
        private var instance: ClassificationDatabase? = null
        fun getInstance(context: Context): ClassificationDatabase =
            instance ?: synchronized(this) {
                instance ?: Room.databaseBuilder(
                    context.applicationContext,
                    ClassificationDatabase::class.java, "Classification.db"
                ).build().also { instance = it }
            }
    }
}