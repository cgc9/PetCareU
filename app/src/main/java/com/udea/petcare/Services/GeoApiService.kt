package com.udea.petcare.Services

import com.udea.petcare.Model.GeoModel
import io.reactivex.Observable
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query


interface GeoApiService {

    @GET("json")
    fun getLocation(@Query("address") address: String,
                      @Query("key") key: String):
            Observable<GeoModel.Result>

    companion object {
        fun create(): GeoApiService {

            val retrofit = Retrofit.Builder()
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl("https://maps.googleapis.com/maps/api/geocode/")
                .build()

            return retrofit.create(GeoApiService::class.java)
        }
    }

}