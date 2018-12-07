package com.navdroid.frshgiph.network

import android.arch.lifecycle.LiveData
import com.navdroid.frshgiph.BuildConfig
import com.navdroid.frshgiph.model.ApiResponse
import com.navdroid.frshgiph.model.GiphyResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query


interface ApiEndPoints {

    @GET("/v1/gifs/search?api_key=${BuildConfig.API_KEY}")
    fun search(@Query("q") query: String, @Query("offset") offset: Int): LiveData<ApiResponse<GiphyResponse>>

    @GET("/v1/gifs/trending?api_key=${BuildConfig.API_KEY}")
    fun trending(@Query("offset") offset: Int): LiveData<ApiResponse<GiphyResponse>>
}