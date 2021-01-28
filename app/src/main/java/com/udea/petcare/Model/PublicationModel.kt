package com.udea.petcare.Model

import com.udea.petcare.Entities.Publication
import com.udea.petcare.Entities.User

object PublicationModel {
    data class Result( var id: Int,
                       var image:String,
                       var date:String,
                       var description: String,
                       var city: String,
                       var type:String,
                       var title: String,
                       var user: User
    )
}