package com.udea.petcare.Fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.udea.petcare.Entities.Publication
import com.udea.petcare.R
import com.udea.petcare.Services.PublicationApiService
import com.udea.petcare.ViewModel.PublicationViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.fragment_losing.view.*
import kotlinx.android.synthetic.main.fragment_recycler_view.view.*
import kotlinx.android.synthetic.main.fragment_recycler_view.view.recyclerPets

class LosingFragment : Fragment(),LosingAdapter.onClickListener{

    lateinit var adapter: LosingAdapter
    private var publicationList:ArrayList<Publication> = ArrayList()
    private var disposable: Disposable? = null
    lateinit var publicationViewModel:PublicationViewModel

    private val publicationApiServe by lazy {
        PublicationApiService.create()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view= inflater.inflate(R.layout.fragment_losing, container, false)
        adapter =LosingAdapter(this)
        val recyclerView=view.recyclerLosing
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        publicationViewModel=ViewModelProvider(this).get(PublicationViewModel::class.java)

        getPublications()

        return view

    }

    private fun getPublications() {
        var publication:Publication
       // publicationViewModel.deleteAllPublications()

        disposable = publicationApiServe.getPublicationsByType("Perdida")
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { result ->
                    for(i: Int in result.indices){
                        publication = Publication(
                            0,
                            result[i].title,
                            result[i].description,
                            result[i].city,
                            result[i].type,
                            result[i].date,
                            result[i].image,
                            result[i].user.id
                        )

                        Log.d("publication",result.toString())
                        Log.d("publication",publication.toString())
                        publicationList.add(publication)
                        //publicationViewModel.addPublication(publication)
                    }
                    Log.d("Lista publication",publicationList.toString())
                    adapter.setData(publicationList)
                },
                { error -> Toast.makeText(requireContext(), error.message, Toast.LENGTH_LONG).show()
                    error.message?.let { Log.d("error", it) }
                }
            )
    }

}