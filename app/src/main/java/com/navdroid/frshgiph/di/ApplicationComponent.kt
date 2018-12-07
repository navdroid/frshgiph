package com.navdroid.frshgiph.di

import com.navdroid.frshgiph.MainApplication
import dagger.Component
import dagger.android.AndroidInjectionModule
import dagger.android.AndroidInjector


@Component(modules = [
    AndroidInjectionModule::class,
    ApplicationModule::class,
    ActivityBuilder::class])
interface ApplicationComponent : AndroidInjector<MainApplication> {
    @Component.Builder
    abstract class Builder : AndroidInjector.Builder<MainApplication>()

}