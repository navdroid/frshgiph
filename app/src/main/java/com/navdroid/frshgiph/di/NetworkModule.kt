package com.navdroid.frshgiph.di

import android.app.Application
import android.content.Context
import com.navdroid.frshgiph.BuildConfig
import com.navdroid.frshgiph.MainApplication
import com.navdroid.frshgiph.db.GifDb
import com.navdroid.frshgiph.network.ApiEndPoints
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.jackson.JacksonConverterFactory
import java.util.concurrent.TimeUnit

@Module
class NetworkModule {
    val CONNECT_TIMEOUT_IN_MS = 30000

    @Provides
    internal fun provideGithubService(okHttpClient: OkHttpClient): ApiEndPoints {
        return Retrofit.Builder()
                .baseUrl(BuildConfig.BASE_URL)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(okHttpClient)
                .addConverterFactory(JacksonConverterFactory.create())
                .build()
                .create(ApiEndPoints::class.java)
    }

    @Provides
    internal fun provideOkHttpClient(): OkHttpClient {
        val loggingInterceptor = HttpLoggingInterceptor()
        loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY

        return OkHttpClient.Builder()
                .connectTimeout(CONNECT_TIMEOUT_IN_MS.toLong(), TimeUnit.MILLISECONDS)
                .addInterceptor(loggingInterceptor)
                .build()
    }

    @Provides
    fun provideContext(application: MainApplication): Context {
        return application
    }

    @Provides
    fun provideDB(application: MainApplication): GifDb {
        return GifDb.getInstance(application)!!
    }


}