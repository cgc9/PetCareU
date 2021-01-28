package com.udea.petcare.ViewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.udea.petcare.Database.AppDatabase
import com.udea.petcare.Entities.Publication
import com.udea.petcare.Repositories.PublicationRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class PublicationViewModel(application: Application): AndroidViewModel(application)  {

     val readAllData: LiveData<List<Publication>>
    private val repository: PublicationRepository


    init {
        val publicationDao = AppDatabase.getDatabase(application).publicationDao()
        repository = PublicationRepository(publicationDao)
        readAllData = repository.readAllData
    }

    fun addPublication(publication: Publication){
        viewModelScope.launch(Dispatchers.IO) {
            repository.addPublication(publication)
        }

    }

    fun deletePublication(publication: Publication){
        viewModelScope.launch(Dispatchers.IO) {
            repository.deletePublication(publication)
        }

    }

    fun updatePublication(publication: Publication){
        viewModelScope.launch(Dispatchers.IO) {
            repository.updatePublication(publication)
        }

    }

    fun readDataByType(type: String) {
        viewModelScope.launch(Dispatchers.IO) {
           repository.readDataByType(type)
        }
    }

    fun deleteAllPublications(){
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteAllPublication()
        }
    }
}