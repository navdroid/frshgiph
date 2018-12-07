package com.navdroid.frshgiph.di

import com.navdroid.frshgiph.network.ApiEndPoints
import com.navdroid.frshgiph.repos.GiphyRepo
import dagger.Module
import dagger.Provides

@Module
class RepositoryModule {

    @Provides
    fun getNewsRepo(api: ApiEndPoints) = GiphyRepo(api)

}