package com.navdroid.frshgiph

import android.app.Activity
import android.app.Application
import com.navdroid.frshgiph.di.ApplicationComponent
import com.navdroid.frshgiph.di.ApplicationModule
import com.navdroid.frshgiph.di.DaggerApplicationComponent
import com.navdroid.frshgiph.network.ApiEndPoints
import com.orm.SugarContext
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasActivityInjector
import javax.inject.Inject

class MainApplication : Application(), HasActivityInjector {

    @Inject
    lateinit var dispatchingActivityInjector: DispatchingAndroidInjector<Activity>

    override fun activityInjector(): AndroidInjector<Activity> = dispatchingActivityInjector


    override fun onCreate() {
        super.onCreate()
        SugarContext.init(this)
        DaggerApplicationComponent.
                builder().create(this).inject(this)


    }

    override fun onTerminate() {
        SugarContext.terminate()
        super.onTerminate()
    }


}