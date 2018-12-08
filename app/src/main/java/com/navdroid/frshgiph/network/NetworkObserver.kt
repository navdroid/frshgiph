package com.navdroid.frshgiph.network

import io.reactivex.observers.DefaultObserver

abstract class NetworkObserver<T>() : DefaultObserver<T>() {

    abstract fun onPass(response: T)
    abstract fun onFail(e: Throwable)
    override fun onNext(t: T) {
        onPass(t)
    }

    override fun onError(e: Throwable) {
        onFail(e)
    }

    override fun onComplete() {
    }
}
