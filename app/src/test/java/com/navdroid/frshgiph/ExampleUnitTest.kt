package com.navdroid.frshgiph

import android.arch.core.executor.testing.InstantTaskExecutorRule
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.Observer
import com.navdroid.frshgiph.model.Data
import com.navdroid.frshgiph.model.GiphyResponse
import com.navdroid.frshgiph.repos.GiphyRepo
import com.navdroid.frshgiph.repos.Repository
import com.navdroid.frshgiph.viewmodel.MainViewModel
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.verify
import com.nhaarman.mockito_kotlin.whenever
import io.reactivex.Observable
import org.junit.Test

import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
    @Test
    fun addition_isCorrect() {
        assertEquals(4, 2 + 2)
    }

    var giphyRepo: Repository = mock()
    lateinit var viewModel: MainViewModel
    var apiResponseObserver: Observer<ArrayList<Data>?> = mock()
    var data: Observable<GiphyResponse>? = null

    @Rule
    @JvmField
    val instantExecutorRule = InstantTaskExecutorRule()

    @Before
    fun setUp() {
        whenever(giphyRepo.searchGif("xyz", 0)).thenReturn(data)
        viewModel = MainViewModel(giphyRepo)
        viewModel.mGifs.observeForever(apiResponseObserver)
    }

    @Test
    fun testIfEmpty() {
        assert(viewModel.mGifs.value.isNullOrEmpty())
    }

    @Test
    fun testIfNotEmpty() {
        whenever(giphyRepo.searchGif("xyz", 0)).thenReturn(data)
        var mData = Data()
        mData.uid = "xyz"
        mData.imageUrl = "xyz"
        mData.isFavorite = false
        viewModel.mGifs.value = arrayListOf(mData)
        verify(apiResponseObserver).onChanged(arrayListOf(mData))
    }

}
