package com.udea.petcare.Model

object UserModel {
    data class Result( val id: Int,
                       val name: String,
                       val lastName: String,
                       val email: String,
                       val password:String,
                       val city:String,
                       val description:String)
}