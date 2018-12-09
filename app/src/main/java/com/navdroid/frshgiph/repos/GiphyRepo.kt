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

open class GiphyRepo @Inject constructor(val api: ApiEndPoints, val db: GifDb) : Repository {
    private var mDisposables = CompositeDisposable()

    override fun searchGif(query: String, offset: Int): Observable<GiphyResponse> {

        val remoteObservable = (if (query.isEmpty())
            api.trending(offset = if (offset == 0) null else offset)
        else
            api.search(query = query, offset = if (offset == 0) null else offset))


        val favoriteObservable = db.gifDao().getAllFavorite().toObservable()

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

    override fun update(gif: Data) {
        mDisposables.add(Observable.fromCallable {
            if (gif.isFavorite)
                db.gifDao().insert(gif)
            else
                db.gifDao().delete(gif)
        }
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .subscribe {

                })
    }

    override fun getFavorite(): Observable<List<Data>> {
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

    override fun clear() {
        mDisposables.clear()
    }


}