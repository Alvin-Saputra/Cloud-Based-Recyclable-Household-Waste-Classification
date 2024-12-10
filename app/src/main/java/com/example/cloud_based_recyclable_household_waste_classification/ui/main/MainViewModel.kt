package com.example.cloud_based_recyclable_household_waste_classification.ui.main

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.cloud_based_recyclable_household_waste_classification.data.local.ClassificationDatabase
import com.example.cloud_based_recyclable_household_waste_classification.data.pref.UserModel
import com.example.cloud_based_recyclable_household_waste_classification.data.pref.UserRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainViewModel (private val repository: UserRepository, application: Application) : AndroidViewModel(application) {
    private val eventDao = ClassificationDatabase.getInstance(application).classificationDao()

    fun getSession(): LiveData<UserModel> {
        return repository.getSession().asLiveData()
    }

    fun DeleteAllFromDatabase() {
        viewModelScope.launch(Dispatchers.IO) {
          eventDao.deleteAll()
        }
    }





}