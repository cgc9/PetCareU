package com.udea.petcare.Repositories


import androidx.lifecycle.LiveData
import com.udea.petcare.Dao.UserDao
import com.udea.petcare.Entities.User

class UserRepository(private val userDao: UserDao)  {
    val readAllData: LiveData<List<User>> = userDao.readAllData()
    val count: LiveData<Int> = userDao.countUser()

    suspend fun addUser(user: User){
        userDao.addUser(user)
    }

    fun findUserByEmail(email: String): LiveData<User> {
        return userDao.findUserByEmail(email)
    }

    fun findUserByE(userName: String): User {
        return userDao.findUserByE(userName)
    }

    fun deleteAllUsers(){
        return userDao.deleteAllUsers()
    }
}