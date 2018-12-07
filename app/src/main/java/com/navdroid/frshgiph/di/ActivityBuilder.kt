package com.navdroid.frshgiph.di

import com.navdroid.frshgiph.ui.MainActivity
import com.navdroid.frshgiph.ui.MainActivityModule
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class ActivityBuilder {
    @ContributesAndroidInjector(modules = [MainActivityModule::class])
    abstract fun contributeMainActivity(): MainActivity
}