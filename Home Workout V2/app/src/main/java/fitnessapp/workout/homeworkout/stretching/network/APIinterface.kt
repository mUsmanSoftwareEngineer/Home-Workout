package com.admision.network


import fitnessapp.workout.homeworkout.stretching.viewmodel.AdsIdDataResponse
import retrofit2.Call
import retrofit2.http.*


interface APIinterface {

    @GET("advertisement/17")
    fun getAdvertisementID(): Call<ArrayList<AdsIdDataResponse>>
}