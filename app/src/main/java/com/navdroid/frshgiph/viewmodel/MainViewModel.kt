package com.navdroid.frshgiph.viewmodel

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.Observer
import android.arch.lifecycle.Transformations
import android.arch.lifecycle.ViewModel
import android.view.View
import com.navdroid.frshgiph.MainApplication
import com.navdroid.frshgiph.Utils.SingleLiveEvent
import com.navdroid.frshgiph.Utils.switchMapForApiResponse
import com.navdroid.frshgiph.model.ApiResponse
import com.navdroid.frshgiph.model.GiphyResponse
import com.navdroid.frshgiph.repos.GiphyRepo
import javax.inject.Inject

class MainViewModel @Inject constructor(var giphyRepo: GiphyRepo) : ViewModel() {


    fun getTrend(): LiveData<ApiResponse<GiphyResponse>> {

        return giphyRepo.getTrendyGif()
    }

}