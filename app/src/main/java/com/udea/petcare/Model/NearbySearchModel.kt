package com.udea.petcare.Model

object NearbySearchModel {

    data class Result(val results:ArrayList<Results>)
    data class Results(val geometry:Geometry, val name:String, val vicinity:String)
    data class Geometry(val location:Location)
    data class Location(val lat:String,val lng:String)

}