package com.navdroid.frshgiph.viewmodel

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.navdroid.frshgiph.model.Data
import com.navdroid.frshgiph.model.GiphyResponse
import com.navdroid.frshgiph.network.NetworkObserver
import com.navdroid.frshgiph.repos.GiphyRepo
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class MainViewModel @Inject constructor(var giphyRepo: GiphyRepo) : ViewModel() {

    private var mQuery: String = ""
    private var mOffset: Int = 0
    private var mTotalCount = -1
    private var mDisposables = CompositeDisposable()
    var mGifs = MutableLiveData<ArrayList<Data>>()
    var mFavGifs = MutableLiveData<ArrayList<Data>>()

    fun getGifs(query: String = "", isNext: Boolean = false) {
        when {
            query.isEmpty() -> mOffset = if (isNext) mOffset + 25 else 0
            query == mQuery && isNext -> mOffset += 25
            else -> {
                mOffset = 0
                mQuery = query
            }
        }
        if (mTotalCount > mOffset || mOffset == 0) {
            giphyRepo.searchGif(query, mOffset)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(object : NetworkObserver<GiphyResponse>() {
                        override fun onPass(response: GiphyResponse) {
                            if (response.meta?.status == 200) {
                                mGifs.value = response.data
                                mTotalCount = response.pagination?.totalCount ?: -1
                            }
                        }
                        override fun onFail(e: Throwable) {
                        }
                    })
        }
    }

    fun getFavGifs() {
        mDisposables.add(giphyRepo.getFavorite()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe {
                            mFavGifs.value = it as ArrayList<Data> ?: arrayListOf()
                })
    }

    fun updateFavGif(gif: Data) {
        mDisposables.add(giphyRepo.getById(gif.uid)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe {
                    if (it != null && it.isNotEmpty()) {
                        it[0].let {
                            it.isFavorite = !it.isFavorite
                            giphyRepo.update(it.uid, it.isFavorite)
                            mGifs.update(it)
                        }
                    }
                })
    }

    fun MutableLiveData<ArrayList<Data>>.update(value: Data) {
        val list = this.value ?: arrayListOf()
        if (list.contains(value))
            list[list.indexOf(value)] = value
        this.postValue(list)
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

    override fun onCleared() {
        mDisposables.clear()
        giphyRepo.clear()
        super.onCleared()

    }

}