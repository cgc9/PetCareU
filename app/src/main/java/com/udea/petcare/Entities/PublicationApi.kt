package com.udea.petcare.Entities

data class PublicationApi (
    var id: Int,
    var title: String,
    var description: String,
    var city: String,
    var type:String,
    var dateTime:String,
    var image:String,
    var user:User
)