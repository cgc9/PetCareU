package com.udea.petcare.Fragments

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.udea.petcare.Entities.Place
import com.udea.petcare.R
import kotlinx.android.synthetic.main.fragment_day_care.view.*
import kotlinx.android.synthetic.main.fragment_veterinary.view.*


class DayCareFragment : Fragment(), DayCareAdapter.onClickListener{

    val dayCarePlaces:ArrayList<Place> = ArrayList()
    val namePlace= listOf<String>("Club Family Dogs","Privilege Dog","Pet City","Animal care","Perrino Guarderia Campestre","Gatografía - Niñera de gatos","Las Nanas Guarderia Canina","Rancho San Isidro Guardería Canina","GUARDERIA SAN MARTIN","Colegio Canino Nawa")
    val dirPlace= listOf<String>("Cra. 71 #32b126, Medellín, Antioquia","Cl. 37 ## 64a -14, Medellín, Antioquia","Cl. 16b Sur #25B 17, Medellín, Antioquia","Av. 33 #83A-7, Medellín, Antioquia","Vereda el Jardin, 0500, Medellín, Antioquia","","Cl. 37B ##84B - 56, Medellín, Antioquia","","ESTRELLA DE MAR, La Estrella, Antioquia","Cl. 36 Sur ###22-05, Envigado, Antioquia")
    val uriPlace= listOf<String>("geo:".plus("<6.2368989>,<-75.5940204>?q=<6.2368989>,<-75.5940204>"),"geo:".plus("<6.2418125>,<-75.5840742>?q=<6.2418125>,<-75.5840742>"),"geo:".plus("<6.1842127>,<-75.5659298>?q=<6.1842127>,<-75.5659298>"),"geo:".plus("<6.2384279>,<-75.6090058>?q=<6.2384279>,<-75.6090058>"),"geo:".plus("<6.2048913>,<-75.6258157>?q=<6.2048913>,<-75.6258157>"),"geo:".plus("<6.2256075>,<-75.5855889>?q=<6.2256075>,<-75.5855889>"),"geo:".plus("<6.2472204>,<-75.6083975>?q=<6.2472204>,<-75.6083975>"),"geo:".plus("<6.2168146>,<-75.6501331>?q=<6.2168146>,<-75.6501331>"),"geo:".plus("<6.1516794>,<-75.6459114,15>?q=<6.1516794>,<-75.6459114,15>"),"geo:".plus("<6.1616149>,<-75.5902072>?q=<6.1616149>,<-75.5902072>"))
    var placesList:ArrayList<Place> = ArrayList()
    lateinit var adapter: DayCareAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view=inflater.inflate(R.layout.fragment_day_care, container, false)

        for (i in 0..namePlace.size-1) {

            var place=Place(namePlace[i],dirPlace[i], Uri.parse(uriPlace[i]))
            placesList.add(place)

        }

        adapter = DayCareAdapter(placesList, this)
        val recyclerView=view.recyclerDayCare
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        Log.d("lista",placesList.toString())

        Log.d("garNa",namePlace.size.toString())
        Log.d("gardir",dirPlace.size.toString())
        Log.d("garUri",uriPlace.size.toString())

        return view


    }

    override fun onItemClick(item: Place, index: Int) {
        val intent = Intent(Intent.ACTION_VIEW).apply {
            data =item.geoLocation
        }
        // intent.setPackage("com.google.android.apps.maps")
        startActivity(intent)
    }


}
