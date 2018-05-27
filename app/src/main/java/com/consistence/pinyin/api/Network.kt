package com.consistence.pinyin.api

import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import com.squareup.moshi.KotlinJsonAdapterFactory
import com.squareup.moshi.Moshi
import dagger.Module
import dagger.Provides
import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Converter
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

interface SchedulerProvider {
    fun main(): Scheduler
    fun thread(): Scheduler
}

@Module
class NetworkModule {

    @Provides
    @Singleton
    fun schedules(): SchedulerProvider {
        return object: SchedulerProvider {
            override fun main(): Scheduler {
                return AndroidSchedulers.mainThread()
            }

            override fun thread(): Scheduler {
                return Schedulers.io()
            }
        }
    }

    @Singleton @Provides
    fun okhttpClient(): OkHttpClient {
        return OkHttpClient.Builder()
                .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
                .connectTimeout(10, TimeUnit.SECONDS)
                .readTimeout(10, TimeUnit.SECONDS)
                .writeTimeout(10, TimeUnit.SECONDS)
                .build();
    }


    @Singleton @Provides
    fun moshi() : Moshi {
        return Moshi.Builder()
                .add(KotlinJsonAdapterFactory())
                .build()
    }

    @Singleton @Provides
    fun converterFactory(moshi: Moshi) : Converter.Factory {
        return MoshiConverterFactory.create(moshi);
    }

    @Singleton @Provides
    fun retrofit(httpClient: OkHttpClient, converterFactory: Converter.Factory) : Retrofit {
        return Retrofit.Builder()
                .baseUrl("http://192.168.0.13:8000/")
                .client(httpClient)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(converterFactory)
                .build()
    }
}