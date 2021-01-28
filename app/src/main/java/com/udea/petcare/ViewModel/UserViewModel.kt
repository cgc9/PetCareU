package com.udea.petcare.ViewModel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.udea.petcare.Database.AppDatabase
import com.udea.petcare.Entities.User
import com.udea.petcare.Repositories.UserRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class UserViewModel(application: Application): AndroidViewModel(application)  {

    val readAllData: LiveData<List<User>>
    val count: LiveData<Int>
    private val repository: UserRepository

    init {
        val userDao = AppDatabase.getDatabase(application).userDao()
        repository = UserRepository(userDao)
        count= repository.count
        readAllData = repository.readAllData

    }

    fun findUserByE(email: String): User {
        Log.d("EmailUsuario",email)
        lateinit var user:User
        viewModelScope.launch(Dispatchers.IO) {
            user= repository.findUserByE(email)
            Log.d("UsuarioLoco",user.toString())
        }
        return user
    }

    fun findUserByEmail(email: String)=repository.findUserByEmail(email)

    fun addUser(user: User){
        viewModelScope.launch(Dispatchers.IO) {
            repository.addUser(user)
        }

    }

    fun deleteAllUsers() {
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteAllUsers()
        }
    }


}