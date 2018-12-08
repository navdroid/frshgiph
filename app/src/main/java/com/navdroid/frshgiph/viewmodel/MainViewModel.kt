package com.navdroid.frshgiph.viewmodel

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.Observer
import android.arch.lifecycle.Transformations
import android.arch.lifecycle.ViewModel
import android.support.v7.widget.GridLayoutManager
import android.util.Log
import android.view.View
import com.navdroid.frshgiph.MainApplication
import com.navdroid.frshgiph.model.ApiResponse
import com.navdroid.frshgiph.model.Data
import com.navdroid.frshgiph.model.GiphyResponse
import com.navdroid.frshgiph.model.Pagination
import com.navdroid.frshgiph.repos.GiphyRepo
import com.orm.SugarDb
import com.orm.SugarRecord
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject

class MainViewModel @Inject constructor(var giphyRepo: GiphyRepo) : ViewModel() {

    private var mQuery: String = ""
    private var mOffset: Int = 0
    var mGifs = MutableLiveData<ArrayList<Data>>()
    var mFavGifs = MutableLiveData<ArrayList<Data>>()
    private var totalCount = -1

    fun getGifs(query: String = "", isNext: Boolean = false) {
        when {
            query.isEmpty() -> mOffset = if (isNext) mOffset + 25 else 0
            query == mQuery && isNext -> mOffset += 25
            else -> {
                mOffset = 0
                mQuery = query
            }
        }
        if (totalCount > mOffset || mOffset == 0) {
            giphyRepo.searchGif(query, mOffset)
                    .enqueue(object : Callback<GiphyResponse> {
                        override fun onFailure(call: Call<GiphyResponse>, t: Throwable) {
                            t.printStackTrace()
                        }

                        override fun onResponse(call: Call<GiphyResponse>, response: Response<GiphyResponse>) {
                            if (response.body()?.meta?.status == 200) {
                                mGifs.value = response.body()?.data
                                totalCount = response.body()?.pagination?.totalCount ?: -1
                            }
                        }

                    })
        }
    }

    fun getFavGifs() {
        mFavGifs.value = SugarRecord.listAll(Data::class.java) as ArrayList<Data>
    }

    fun updateFavGif(gif: Data) {
        val saveGif = SugarRecord.find(Data::class.java, "uid = ?", gif.uid)

        if (saveGif != null && saveGif.isNotEmpty()) {
            saveGif[0].delete()
            mFavGifs.remove(saveGif[0])
        } else {
            gif.save()
            mFavGifs.add(gif)
        }


    }


    fun MutableLiveData<ArrayList<Data>>.add(values: Data) {
        val value = this.value ?: arrayListOf()
        value.add(values)
        this.value = value
    }

    fun MutableLiveData<ArrayList<Data>>.remove(gif: Data) {
        val value = this.value ?: arrayListOf()
        value.remove(gif)
        this.value = value
    }

}