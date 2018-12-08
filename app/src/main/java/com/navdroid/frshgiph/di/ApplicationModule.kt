package com.navdroid.frshgiph.di

import com.navdroid.frshgiph.repos.GiphyRepo
import com.navdroid.frshgiph.viewmodel.MainViewModelFactory
import dagger.Module
import dagger.Provides


@Module(includes = [NetworkModule::class, RepositoryModule::class])
class ApplicationModule {

}