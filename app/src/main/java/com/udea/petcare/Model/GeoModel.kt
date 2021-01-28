package com.udea.petcare.Model

import android.location.Location


object GeoModel {
    data class Result(val results:ArrayList<Results>)
    data class Results(val geometry:Geometry)
    data class Geometry(val location:Location)
    data class Location(val lat:String,val lng:String)

}