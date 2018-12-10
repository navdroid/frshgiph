package com.navdroid.frshgiph.viewmodel

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModel
import com.navdroid.frshgiph.Utils
import com.navdroid.frshgiph.model.Data
import com.navdroid.frshgiph.model.GiphyResponse
import com.navdroid.frshgiph.network.NetworkObserver
import com.navdroid.frshgiph.repos.GiphyRepo
import com.navdroid.frshgiph.repos.Repository
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.functions.Consumer
import io.reactivex.schedulers.Schedulers
import org.reactivestreams.Subscriber
import org.reactivestreams.Subscription
import javax.inject.Inject

const val OFFSET = 10

class MainViewModel @Inject constructor(var giphyRepo: Repository) : ViewModel() {

    private var mQuery: String = ""
    private var mOffset: Int = 0
    private var mTotalCount = -1
    private var mDisposables = CompositeDisposable()
    private var mApiDisposable = CompositeDisposable()
    var error = MutableLiveData<String>()
    var mGifs = MutableLiveData<ArrayList<Data>>()
    var mFavGifs = MutableLiveData<ArrayList<Data>>()

    fun getGifs(query: String = "", isNext: Boolean = false) {
        when {
            query.isEmpty() -> mOffset = if (isNext) mOffset + OFFSET else 0
            query == mQuery && isNext -> mOffset += OFFSET
            else -> {
                mOffset = 0
                mQuery = query
            }
        }
        if (mTotalCount > mOffset || mOffset == 0) {
            mApiDisposable.clear()
            mApiDisposable.add(giphyRepo.searchGif(query, mOffset)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe({

                        if (it.meta?.status == 200) {
                            error.value = null
                            if (it.pagination?.offset != null && it.pagination?.offset!! > 0)
                                mGifs.add(it.data)
                            else {
                                mGifs.postValue(it.data)
                            }

                            mTotalCount = it.pagination?.totalCount ?: -1
                        } else
                            error.value = it.meta?.msg
                    }, {
                        error.value = it.message
                        mGifs.postValue(arrayListOf())
                    })
            )
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
        gif.let {
            it.isFavorite = !it.isFavorite
            giphyRepo.update(it)
            mGifs.update(it)
        }
    }

    fun MutableLiveData<ArrayList<Data>>.update(value: Data) {
        val list = this.value ?: arrayListOf()
        if (list.contains(value))
            list[list.indexOf(value)] = value
        this.postValue(list)
    }


    fun MutableLiveData<ArrayList<Data>>.add(values: ArrayList<Data>) {
        val value = this.value ?: arrayListOf()
        value.addAll(values)
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