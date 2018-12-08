package com.navdroid.frshgiph.repos

import com.navdroid.frshgiph.db.GifDb
import com.navdroid.frshgiph.model.Data
import com.navdroid.frshgiph.model.GiphyResponse
import com.navdroid.frshgiph.network.ApiEndPoints
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.functions.BiFunction
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class GiphyRepo @Inject constructor(val api: ApiEndPoints, val db: GifDb) {
    private var mDisposables = CompositeDisposable()

    fun searchGif(query: String = "", offset: Int): Observable<GiphyResponse> {
        val remoteObservable = (if (query.isEmpty())
            api.trending(offset = offset)
        else
            api.search(query = query, offset = offset))
                .doOnNext {
                    if (it.data.isNotEmpty())
                        insert(it.data)
                }
        val favoriteObservable = getFavorite()

        return Observable.zip(remoteObservable, favoriteObservable, BiFunction { remote: GiphyResponse, local: List<Data> ->
            remote.data.forEach {
                if (local.contains(it))
                    it.isFavorite = true
            }
            remote
        })
    }

    private fun insert(list: ArrayList<Data>) {
        mDisposables.add(Observable.fromCallable {
            db.gifDao().insert(list)
        }
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .subscribe {

                })
    }

    fun getFavorite(): Observable<List<Data>> {
        return db.gifDao().getAllFavorite().toObservable()
    }

    fun getById(id: String): Observable<List<Data>> {
        return db.gifDao().getById(id).toObservable()
    }

    fun update(id: String, isFavorite: Boolean) {
        mDisposables.add(Observable.fromCallable {
            db.gifDao().updateLoads(id, isFavorite)
        }
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .subscribe {

                })
    }

    fun clear() {
        mDisposables.clear()
    }


}