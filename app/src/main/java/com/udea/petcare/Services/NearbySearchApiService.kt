package com.udea.petcare.Services

import com.udea.petcare.Model.GeoModel
import com.udea.petcare.Model.NearbySearchModel
import io.reactivex.Observable
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

interface NearbySearchApiService {

    @GET("json")
    fun getPlaces(
        @Query("location") location: String,
        @Query("radius") radius: String,
        @Query("types")types:String,
        @Query("key") key: String
    ):
            Observable<NearbySearchModel.Result>
    @GET("json")

    fun getLatLog(
        @Query("location") location: String,
        @Query("radius") radius: String,
        @Query("types")types:String,
        @Query("key") key: String
    ):
            Observable<GeoModel.Result>

    companion object {
        fun create(): NearbySearchApiService {

            val retrofit = Retrofit.Builder()
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl("https://maps.googleapis.com/maps/api/place/nearbysearch/")
                .build()

            return retrofit.create(NearbySearchApiService::class.java)
        }
    }
}