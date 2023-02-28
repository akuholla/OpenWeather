package com.akshayholla.openweather.data.network

import com.akshayholla.openweather.common.baseUrl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {
    @Provides
    @Named("weather_base_url")
    fun provideBaseUrl(): String = baseUrl

    @Singleton
    @Provides
    fun providesOkHttpClient(): OkHttpClient = OkHttpClient.Builder().build()

    @Singleton
    @Provides
    fun providesRetrofit(
        okHttpClient: OkHttpClient,
        @Named("weather_base_url") baseUrl: String
    ): Retrofit =
        Retrofit.Builder().addConverterFactory(GsonConverterFactory.create()).baseUrl(baseUrl)
            .client(okHttpClient).build()

    @Singleton
    @Provides
    fun providesGiphyApiService(retrofit: Retrofit): OpenWeatherApi =
        retrofit.create(OpenWeatherApi::class.java)

}