package com.navdroid.frshgiph.ui

import com.navdroid.frshgiph.repos.GiphyRepo
import com.navdroid.frshgiph.viewmodel.MainViewModelFactory
import dagger.Module
import dagger.Provides

@Module
class MainActivityModule {

    @Provides
    internal fun provideViewModelFactory(repository: GiphyRepo) : MainViewModelFactory
            = MainViewModelFactory(repository)
}