package com.navdroid.frshgiph.repos

import android.arch.lifecycle.LiveData
import com.navdroid.frshgiph.model.Data
import com.navdroid.frshgiph.model.GiphyResponse
import io.reactivex.Observable

interface Repository {
    fun searchGif(query: String = "", offset: Int): Observable<GiphyResponse>

    fun update(gif: Data)
    fun getFavorite(): Observable<List<Data>>
    fun clear()
}