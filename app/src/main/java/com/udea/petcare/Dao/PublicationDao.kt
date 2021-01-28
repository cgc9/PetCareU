package com.udea.petcare.Dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.udea.petcare.Entities.Publication
import com.udea.petcare.Entities.User

@Dao
interface PublicationDao {


    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addPublication(publication: Publication)

    @Query("SELECT * FROM publication_table ORDER BY dateTime DESC")
    fun readAllData(): LiveData<List<Publication>>

    @Query("SELECT * FROM publication_table WHERE type LIKE :publicationType")
    fun readDataByType(publicationType:String): LiveData<List<Publication>>

    @Query("DELETE FROM publication_table")
    fun deleteAllPublications()

    @Update
    fun updatePublication(publication: Publication)

    @Delete
    suspend fun deletePublication(publication: Publication)
}