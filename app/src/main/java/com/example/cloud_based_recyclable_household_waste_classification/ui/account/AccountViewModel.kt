package com.example.cloud_based_recyclable_household_waste_classification.ui.account

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

class AccountViewModel(private val repository: UserRepository, application: Application) : AndroidViewModel(application) {

//    private val _text = MutableLiveData<String>().apply {
//        value = "This is notifications Fragment"
//    }
//    val text: LiveData<String> = _text

    fun getSession(): LiveData<UserModel> {
        return repository.getSession().asLiveData()
    }

    private val eventDao = ClassificationDatabase.getInstance(application).classificationDao()

    fun DeleteAllFromDatabase() {
        viewModelScope.launch(Dispatchers.IO) {
           eventDao.deleteAll()
        }
    }

    fun logout() {
        viewModelScope.launch {
            repository.logout()
        }
    }
}