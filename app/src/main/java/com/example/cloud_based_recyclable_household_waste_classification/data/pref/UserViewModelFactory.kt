package com.example.cloud_based_recyclable_household_waste_classification.data.pref

import android.app.Application
import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.cloud_based_recyclable_household_waste_classification.di.Injection
import com.example.cloud_based_recyclable_household_waste_classification.ui.account.AccountViewModel
import com.example.cloud_based_recyclable_household_waste_classification.ui.detail.DetailViewModel
import com.example.cloud_based_recyclable_household_waste_classification.ui.home.HomeViewModel
import com.example.cloud_based_recyclable_household_waste_classification.ui.login.LoginViewModel
import com.example.cloud_based_recyclable_household_waste_classification.ui.main.MainViewModel
import com.example.cloud_based_recyclable_household_waste_classification.ui.saved.SavedViewModel

class UserViewModelFactory(private val repository: UserRepository,  private val application: Application) :
    ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(LoginViewModel::class.java) -> {
                LoginViewModel(repository) as T
            }

            modelClass.isAssignableFrom(MainViewModel::class.java) -> {
                MainViewModel(repository, application) as T
            }

            modelClass.isAssignableFrom(HomeViewModel::class.java) -> {
                HomeViewModel(repository) as T
            }

            modelClass.isAssignableFrom(AccountViewModel::class.java) -> {
                AccountViewModel(repository, application) as T
            }


            modelClass.isAssignableFrom(SavedViewModel::class.java) -> {
                SavedViewModel(repository, application) as T
            }

            modelClass.isAssignableFrom(DetailViewModel::class.java) -> {
                DetailViewModel(repository) as T
            }


            else -> throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
        }
    }

    companion object {
        @Volatile
        private var INSTANCE: UserViewModelFactory? = null

        @JvmStatic
        fun getInstance(context: Context, application: Application): UserViewModelFactory {
            if (INSTANCE == null) {
                synchronized(UserViewModelFactory::class.java) {
                    INSTANCE = UserViewModelFactory(Injection.provideUserRepository(context), application)
                }
            }
            return INSTANCE as UserViewModelFactory
        }
    }
}