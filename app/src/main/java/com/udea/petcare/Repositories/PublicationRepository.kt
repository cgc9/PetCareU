package com.udea.petcare.Repositories

import androidx.lifecycle.LiveData
import com.udea.petcare.Dao.PublicationDao
import com.udea.petcare.Entities.Publication

class PublicationRepository(private val publicationDao: PublicationDao)  {
    val readAllData: LiveData<List<Publication>> = publicationDao.readAllData()
    //val readByDate: LiveData<List<Publication>> = publicationDao.readDataByType(String:type)

    suspend fun addPublication(publication: Publication){
        publicationDao.addPublication(publication)
    }

   fun readDataByType(publicationType:String): LiveData<List<Publication>> {
        return publicationDao.readDataByType(publicationType)
    }

    suspend fun deletePublication(publication: Publication){
       return publicationDao.deletePublication(publication)
    }

    fun updatePublication(publication: Publication){
        return publicationDao.updatePublication(publication)
    }

    fun deleteAllPublication(){
        return publicationDao.deleteAllPublications()
    }

}