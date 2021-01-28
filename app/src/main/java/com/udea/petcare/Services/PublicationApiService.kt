package com.udea.petcare.Services

import com.squareup.okhttp.ResponseBody
import com.udea.petcare.Entities.Publication
import com.udea.petcare.Entities.PublicationApi
import com.udea.petcare.Model.PublicationModel
import com.udea.petcare.Model.UserModel
import io.reactivex.Observable
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*

interface PublicationApiService {
    @GET("allPublications")
    fun getPublications(): Observable<List<PublicationModel.Result>>

    @GET("byType/{type}")
    fun getPublicationsByType(@Path("type") type:String):
            Observable<List<PublicationModel.Result>>

    @POST("createPublication")
    fun createPublication(@Body publicationData: PublicationApi): Call<PublicationApi>

    @GET("byId/{id}")
    fun getUserById(@Path("id") id: Int):
            Observable<PublicationModel.Result>

    @HTTP(method = "DELETE", path = "deletePublication", hasBody = true)
    fun deletePublication(@Body publicationData: PublicationApi): Call<PublicationApi>


    companion object {
        fun create():PublicationApiService {

            val retrofit = Retrofit.Builder()
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl("https://petcareudea.herokuapp.com/publications/")
                .build()

            return retrofit.create(PublicationApiService::class.java)
        }
    }
}