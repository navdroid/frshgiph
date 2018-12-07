package com.navdroid.frshgiph.di

import com.example.rohitsingh.news.network.LiveDataCallAdapterFactory
import com.navdroid.frshgiph.BuildConfig
import com.navdroid.frshgiph.network.ApiEndPoints
import dagger.Module
import dagger.Provides
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.IOException
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
class NetworkModule{
    val CONNECT_TIMEOUT_IN_MS = 30000

//    @Singleton
    @Provides
    internal fun provideGithubService(okHttpClient: OkHttpClient): ApiEndPoints {
        return Retrofit.Builder()
                .baseUrl(BuildConfig.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(LiveDataCallAdapterFactory())
                .client(okHttpClient)
                .build()
                .create(ApiEndPoints::class.java)
    }

    @Provides
//    @Singleton
    internal fun provideOkHttpClient(): OkHttpClient {
        val loggingInterceptor = HttpLoggingInterceptor()
        loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY

        return OkHttpClient.Builder()
                .connectTimeout(CONNECT_TIMEOUT_IN_MS.toLong(), TimeUnit.MILLISECONDS)
                .addInterceptor(loggingInterceptor)
                .build()
    }

}