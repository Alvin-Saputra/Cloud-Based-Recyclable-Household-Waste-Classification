package com.example.cloud_based_recyclable_household_waste_classification.data.pref

data class UserModel (
    val email: String,
    val username: String,
    val exp: Int,
    val token: String,
    val isLogin: Boolean = false
)