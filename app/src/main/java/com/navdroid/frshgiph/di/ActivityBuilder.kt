package com.navdroid.frshgiph.di

import com.navdroid.frshgiph.ui.FavouriteFragment
import com.navdroid.frshgiph.ui.MainActivity
import com.navdroid.frshgiph.ui.MainActivityModule
import com.navdroid.frshgiph.ui.TrendingFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class ActivityBuilder {
    @ContributesAndroidInjector(modules = [MainActivityModule::class])
    abstract fun contributeMainActivity(): MainActivity

    @ContributesAndroidInjector(modules = [MainActivityModule::class])
    abstract fun contributeTrendingFragment(): TrendingFragment


    @ContributesAndroidInjector(modules = [MainActivityModule::class])
    abstract fun contributeFavouriteFragment(): FavouriteFragment

}