package com.udea.petcare.Fragments

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.udea.petcare.Entities.Place
import com.udea.petcare.R
import com.udea.petcare.Services.GeoApiService
import com.udea.petcare.Services.NearbySearchApiService
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_veterinary.view.*


class VeterinaryFragment : Fragment(),VeterinaryAdapter.onClickListener {

    private var disposable: Disposable? = null
    private  var latitud:String=""
    private var longitud:String=""
    lateinit var adapter: VeterinaryAdapter
    private var key: String="AIzaSyBQUEM9lItoxsh5682YP5w7p_4ArDb9Fws"
    val veterinaryList = ArrayList<Place>()
    private var uri="geo:"


    private val GeoApiServe by lazy {
        GeoApiService.create()
    }

    private val NearbySearchApiServe by lazy {
        NearbySearchApiService.create()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view=inflater.inflate(R.layout.fragment_veterinary, container, false)
        nearbySearch("6.2677479,-75.5688416")
        Handler().postDelayed(Runnable {
            adapter = VeterinaryAdapter(veterinaryList, this)
            val recyclerView=view.recyclerVeterinary
            recyclerView.adapter = adapter
            recyclerView.layoutManager = LinearLayoutManager(requireContext())
        }, 1000)

        return view
    }

    private fun searchVeterinary(namePlace: String){
        disposable=GeoApiServe.getLocation(
            "Universidad de Antioquia",
            key
        )
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { result ->
                    latitud = result.results[0].geometry.location.lat
                    longitud = result.results[0].geometry.location.lng
                    Log.d("Latitu", result.results[0].geometry.location.lat)
                    Log.d("Long", result.results[0].geometry.location.lng)
                },
                { error ->
                    Toast.makeText(requireContext(), error.message, Toast.LENGTH_LONG).show()
                    error.message?.let { Log.d("error", it) }
                }
            )

    }

    private fun nearbySearch(location: String){
        disposable=NearbySearchApiServe.getPlaces(
            "6.2677479,-75.5688416",
            "100000",
            "veterinary_care",
            key
        )
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { result ->

                    for (a: Int in 0 until result.results.size) {
                        uri=uri.plus(result.results[a].geometry.location.lat).plus(",").plus(result.results[a].geometry.location.lng)
                        Log.d("Uri",uri)
                      //  Handler().postDelayed(Runnable {

                        var item = Place(result.results[a].name, result.results[a].vicinity,Uri.parse(uri))
                        veterinaryList += item

                       // }, 1000)
                        uri="geo:"
                    }

                },
                { error ->
                    Toast.makeText(requireContext(), error.message, Toast.LENGTH_LONG).show()
                    error.message?.let { Log.d("error", it) }
                }
            )

    }



    override fun onItemClick(item: Place, index: Int) {
        val intent = Intent(Intent.ACTION_VIEW).apply {
            var deli = ":"
            var deli2=","
            val parts = item.geoLocation.toString().split(deli)
            val parts2 = parts[1].split(deli2)
            val longi=parts2[1]
            val lati=parts2[0]
            data = Uri.parse("geo:".plus("<").plus(lati).plus(">,<").plus(longi).plus(">?q=<").plus(lati).plus(">,<").plus(longi))
            Log.d("Split",data.toString())
        }
       // intent.setPackage("com.google.android.apps.maps")
        startActivity(intent)
    }


}