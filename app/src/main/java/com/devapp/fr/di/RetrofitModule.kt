package com.devapp.fr.di

import android.content.Context
import com.devapp.fr.network.GoogleService
import com.devapp.fr.util.storages.SharedPreferencesHelper
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object RetrofitModule {
    @Singleton
    @Provides
    fun provideOkhttpClient() = OkHttpClient.Builder()
        .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
        .readTimeout(3, TimeUnit.SECONDS)
        .connectTimeout(20, TimeUnit.SECONDS)
        .writeTimeout(3, TimeUnit.SECONDS)
        .build()

    @Singleton
    @Provides
    fun provideRetrofit(okHttpClient: OkHttpClient) = Retrofit
        .Builder()
        .baseUrl("https://inputtools.google.com/")
        .client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create(GsonBuilder().setLenient().create()))
        .build()

    @Singleton
    @Provides
    fun provideGoogleService(retrofit: Retrofit) = retrofit.create(GoogleService::class.java)
}