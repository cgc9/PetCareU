package com.udea.petcare.Services

import com.udea.petcare.Entities.User
import com.udea.petcare.Model.GeoModel
import com.udea.petcare.Model.PublicationModel
import com.udea.petcare.Model.UserModel
import io.reactivex.Observable
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*

interface UserApiService {
    @GET("byEmail/{email}")
    fun getUserByEmail(@Path("email") email: String):
            Observable<UserModel.Result>

    @GET("allUsers")
    fun getUsers(): Observable<List<UserModel.Result>>

    @POST("newUser")
    fun createUser(@Body userData: User): Call<User>


    companion object {
        fun create():UserApiService {

            val retrofit = Retrofit.Builder()
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl("https://petcareudea.herokuapp.com/users/")
                .build()

            return retrofit.create(UserApiService::class.java)
        }
    }
}