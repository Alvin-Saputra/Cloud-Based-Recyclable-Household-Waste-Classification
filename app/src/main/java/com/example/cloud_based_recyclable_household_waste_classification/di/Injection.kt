package com.example.cloud_based_recyclable_household_waste_classification.di

import android.content.Context
import com.example.cloud_based_recyclable_household_waste_classification.data.pref.UserPreference
import com.example.cloud_based_recyclable_household_waste_classification.data.pref.UserRepository
import com.example.cloud_based_recyclable_household_waste_classification.data.pref.dataStore

object Injection {
    fun provideUserRepository(context: Context): UserRepository {
        val pref = UserPreference.getInstance(context.dataStore)
        return UserRepository.getInstance(pref)
    }
}