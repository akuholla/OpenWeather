package com.akshayholla.openweather.data.network;

import com.akshayholla.openweather.BuildConfig;
import com.akshayholla.openweather.common.ConstantsKt;

import javax.inject.Named;
import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.components.SingletonComponent;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

@Module
@InstallIn(SingletonComponent.class)
public abstract class NetworkModule {
    @Provides
    @Named("weather_base_url")
    public static String provideBaseUrl() {
        return ConstantsKt.baseUrl;
    }

    @Singleton
    @Provides
    public static OkHttpClient providesOkHttpClient() {
        //TODO:  Clean this up
        if (BuildConfig.DEBUG) {
            HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
            logging.setLevel(HttpLoggingInterceptor.Level.BODY);
            return new OkHttpClient()
                    .newBuilder()
                    .addInterceptor(logging)
                    .build();
        } else {
            return new OkHttpClient()
                    .newBuilder()
                    .build();
        }
    }

    @Singleton
    @Provides
    public static Retrofit providesRetrofit(
            OkHttpClient okHttpClient,
            @Named("weather_base_url") String baseUrl
    ) {

        return new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(baseUrl)
                .client(okHttpClient)
                .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
                .build();
    }

    @Singleton
    @Provides
    public static OpenWeatherApi providesGiphyApiService(Retrofit retrofit) {
        return retrofit.create(OpenWeatherApi.class);
    }
}
