package com.navdroid.frshgiph.di

import dagger.Module


@Module(includes = [DataModule::class, RepositoryModule::class])
class ApplicationModule {

}