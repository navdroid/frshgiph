package com.navdroid.frshgiph.repos

import com.navdroid.frshgiph.model.GiphyResponse
import com.navdroid.frshgiph.network.ApiEndPoints
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject

class GiphyRepo @Inject constructor(val api: ApiEndPoints) {


    fun searchGif(query: String = "", offset: Int): Call<GiphyResponse> {
        return if (query.isEmpty())
            api.trending(offset = offset)
        else
            api.search(query = query, offset = offset)
    }


}