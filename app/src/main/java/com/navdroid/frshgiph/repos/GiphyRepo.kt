package com.navdroid.frshgiph.repos

import android.arch.lifecycle.LiveData
import com.navdroid.frshgiph.model.ApiResponse
import com.navdroid.frshgiph.model.GiphyResponse
import com.navdroid.frshgiph.network.ApiEndPoints
import javax.inject.Inject

class GiphyRepo @Inject constructor(val api: ApiEndPoints) {

    fun searchGif(query: String): LiveData<ApiResponse<GiphyResponse>> =
            api.search(query = query, offset = 0)

    fun getTrendyGif(): LiveData<ApiResponse<GiphyResponse>> =
            api.trending(offset = 0)
}