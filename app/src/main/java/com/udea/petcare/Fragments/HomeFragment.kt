package com.udea.petcare.Fragments

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson
import com.udea.petcare.Entities.Publication
import com.udea.petcare.Entities.PublicationApi
import com.udea.petcare.Entities.User
import com.udea.petcare.R
import com.udea.petcare.Services.PublicationApiService
import com.udea.petcare.ViewModel.PublicationViewModel
import es.dmoral.toasty.Toasty
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.dialog_addpublication.view.*
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.fragment_recycler_view.view.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter


class HomeFragment : Fragment(),PetAdapter.onClickListener {

    lateinit var adapter: PetAdapter

    private var publicationSwipe:ArrayList<Publication> = ArrayList()
    private var disposable: Disposable? = null
    lateinit var publicationViewModel:PublicationViewModel
    lateinit var  user: User
    lateinit var type:String


    private val publicationApiServe by lazy {
        PublicationApiService.create()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view= inflater.inflate(R.layout.fragment_home, container, false)
        val preferences = this.getActivity()?.getSharedPreferences(
            "currentUser",
            Context.MODE_PRIVATE
        )
        val gson = Gson()
        val currentUser = preferences!!.getString("user", "")
        user = gson.fromJson(currentUser, User::class.java)
        Log.d("currentUserShare", user.toString())
        adapter = PetAdapter(this, user.id)
        val recyclerView=view.recyclerPets
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        publicationViewModel=ViewModelProvider(this).get(PublicationViewModel::class.java)

        getPublications()


        return view

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        swipe.setOnRefreshListener {
            getPublications()
            swipe.isRefreshing = false

        }
    }

   fun getLocalPublications(){
        var publicationLocal: ArrayList<Publication> = ArrayList()
        val publicationObserver = Observer<List<Publication>> {
            for (publication in it) {
                publicationLocal.add(publication)
            }
        }

        publicationViewModel.readAllData.observe(this, publicationObserver)
        adapter.setData(publicationLocal)
    }


    override fun onItemDelete(item: Publication, index: Int) {
        val mDialog = AlertDialog.Builder(this.getView()?.context)
            .setTitle("Eliminar")
            .setMessage("¿Está seguro de que desea eliminar esta publicación?")
            .setPositiveButton("Si",
                DialogInterface.OnClickListener { dialog, id ->

                    val apiPublication= PublicationApi(item.id,item.title,item.description,item.city,item.type,item.dateTime,item.image,user)
                    Log.d("ApiPublication",apiPublication.toString())
                    publicationApiServe.deletePublication(apiPublication).enqueue(object:
                        Callback<PublicationApi> {
                        override fun onFailure(call: Call<PublicationApi>, t: Throwable) {
                            Log.d("Error","No se pudo eliminar")
                        }

                        override fun onResponse(call: Call<PublicationApi>, response: Response<PublicationApi>) {
                            Log.d("RESPONSE DELETE",response.body().toString())

                        }
                    })

                    publicationViewModel.deletePublication(item)

                    getPublications()
                    Toasty.success(
                        requireContext(),
                        "Publicación eliminada exitosamente",
                        Toast.LENGTH_SHORT
                    ).show()

                    //adapter.notifyItemRemoved(index)
                })

            .setNegativeButton("No") { dialog: DialogInterface?, which: Int ->
                dialog?.dismiss()
            }

        mDialog.show()
    }


    private fun getPublicationSwipe() {
        var publicationSwipe:ArrayList<Publication> = ArrayList()
        var publication:Publication

        disposable = publicationApiServe.getPublications()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { result ->
                    for (i: Int in result.indices) {
                        publication = Publication(
                            result[i].id,
                            result[i].title,
                            result[i].description,
                            result[i].city,
                            result[i].type,
                            result[i].date,
                            result[i].image,
                            result[i].user.id
                        )

                        Log.d("publication", result.toString())
                        Log.d("publication", publication.toString())
                        publicationSwipe.add(publication)
                        //publicationViewModel.addPublication(publication)
                    }
                    Log.d("Lista publication", publicationSwipe.toString())
                    adapter.setData(publicationSwipe)
                },
                { error ->
                    Toast.makeText(requireContext(), error.message, Toast.LENGTH_LONG).show()
                    error.message?.let { Log.d("error", it) }
                }
            )
        publicationSwipe.clear()
    }

    private fun getPublications():ArrayList<Publication> {
        var publicationList:ArrayList<Publication> = ArrayList()
        var publication:Publication
        disposable = publicationApiServe.getPublications()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { result ->
                    for (i: Int in result.indices) {
                        publication = Publication(
                            result[i].id,
                            result[i].title,
                            result[i].description,
                            result[i].city,
                            result[i].type,
                            result[i].date,
                            result[i].image,
                            result[i].user.id
                        )

                        publicationList.add(publication)
                        publicationViewModel.addPublication(publication)
                    }
                    Log.d("Lista publication", publicationList.toString())
                    adapter.setData(publicationList)
                },
                { error ->
                    Toast.makeText(requireContext(), error.message, Toast.LENGTH_LONG).show()
                    error.message?.let { Log.d("error", it) }
                }
            )

        return publicationList
    }


}