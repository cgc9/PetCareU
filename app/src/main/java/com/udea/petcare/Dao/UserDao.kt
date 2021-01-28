package com.udea.petcare.Dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.udea.petcare.Entities.User

@Dao
interface UserDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addUser(user: User)

    @Query("SELECT * FROM user_table")
    fun readAllData(): LiveData<List<User>>

    @Query("SELECT * FROM user_table WHERE email = :email")
    fun findUserByEmail(email:String) : LiveData<User>

    @Query("SELECT * FROM user_table WHERE email LIKE :email")
    fun findUserByE(email:String) :User

    @Query("DELETE FROM user_table")
    fun deleteAllUsers()

    @Query("SELECT COUNT (*) FROM user_table")
    fun countUser(): LiveData<Int>


}