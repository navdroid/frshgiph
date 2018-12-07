package com.navdroid.frshgiph.di

import dagger.Module


@Module(includes = [NetworkModule::class, RepositoryModule::class])
class ApplicationModule {

//    @Provides
//    @ApplicationContext
//    internal fun provideContext(): Context {
//        return mApplication
//    }
//
//    @Provides
//    internal fun provideApplication(): Application {
//        return mApplication
//    }

}