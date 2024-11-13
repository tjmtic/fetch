package com.example.fetchapplication.di;

import com.example.fetchapplication.data.FetchApi
import com.example.fetchapplication.data.ItemRepository
import com.example.fetchapplication.data.ItemRepositoryImpl
import java.util.concurrent.TimeUnit;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.components.SingletonComponent;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

val loggingInterceptor = HttpLoggingInterceptor().apply {
        level =HttpLoggingInterceptor.Level.BODY
    }

@Provides
@Singleton
fun provideHttpClient():OkHttpClient {
    return OkHttpClient.Builder()
            .readTimeout(20, TimeUnit.SECONDS)
            .connectTimeout(20, TimeUnit.SECONDS)
            .addInterceptor(loggingInterceptor)
            .build()
}

@Provides
@Singleton
fun provideBaseRetrofitInstance(okHttpClient:OkHttpClient):Retrofit {
    return Retrofit.Builder()
            .baseUrl("https://fetch-hiring.s3.amazonaws.com/")
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
}

    @Provides
    @Singleton
    fun provideArticleApi(retrofit: Retrofit): FetchApi {
        return retrofit.create(FetchApi::class.java)
    }


    @Provides
    @Singleton
    fun provideArticleRepository(itemApi: FetchApi): ItemRepository {
        return ItemRepositoryImpl(itemApi)
    }
}