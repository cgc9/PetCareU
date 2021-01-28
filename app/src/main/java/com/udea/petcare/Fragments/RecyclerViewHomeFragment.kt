package com.udea.petcare.Fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.udea.petcare.Entities.Publication
import com.udea.petcare.R
import com.udea.petcare.Services.PublicationApiService
import com.udea.petcare.ViewModel.PublicationViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_recycler_view.view.*

/*
class RecyclerViewHomeFragment : Fragment() {

    lateinit var publicationViewModel: PublicationViewModel
    lateinit var adapter: PetAdapter
    private var publicationList:ArrayList<Publication> = ArrayList()
    private var disposable: Disposable? = null


    private val publicationApiServe by lazy {
       PublicationApiService.create()
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view= inflater.inflate(R.layout.fragment_recycler_view, container, false)
        adapter = PetAdapter(this,0)
        val recyclerView=view.recyclerPets

        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        //showPublications()
        getPublications()

        return view
    }

    private fun getPublications() {
        disposable = publicationApiServe.getPublications()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { result ->
                    for(i: Int in result.indices){
                        var publication = Publication(
                            0,
                            result[i].title,
                            result[i].description,
                            result[i].city,
                            result[i].type,
                            result[i].date,
                            result[i].image,
                            result[i].user.id
                        )
                        publicationList.add(publication)
                        //(poiViewModel.addPoint(point)
                    }

                    adapter.setData(publicationList)
                },
                { error -> Toast.makeText(requireContext(), error.message, Toast.LENGTH_LONG).show()
                    error.message?.let { Log.d("error", it) }
                }
            )
    }


    private fun showPublications(){
        //publicationViewModel = ViewModelProvider(this).get(PublicationViewModel::class.java)
        getPublications()



    }

}*/