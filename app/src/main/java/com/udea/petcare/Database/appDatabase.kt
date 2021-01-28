package com.udea.petcare.Database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.udea.petcare.Dao.PublicationDao
import com.udea.petcare.Dao.UserDao
import com.udea.petcare.Entities.Publication
import com.udea.petcare.Entities.User

@Database(entities = [User::class, Publication::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {

    abstract fun userDao(): UserDao
    abstract fun publicationDao():PublicationDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase{
            val tempInstance = INSTANCE
            if(tempInstance != null){
                return tempInstance
            }
            synchronized(this){
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "petCare_database"
                ).build()
                INSTANCE = instance
                return instance
            }
        }
    }

}