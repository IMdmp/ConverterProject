package com.imdmp.converter.di

import com.imdmp.converter.BuildConfig
import com.imdmp.converter.repository.network.AuthorizationNetworkInterceptor
import com.imdmp.converter.repository.network.ConverterService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.scalars.ScalarsConverterFactory

@InstallIn(SingletonComponent::class)
@Module
class NetworkModule {

    @Provides
    fun provideOkHttpClient(): OkHttpClient {
        val logging = HttpLoggingInterceptor()
        logging.setLevel(HttpLoggingInterceptor.Level.BASIC)
        logging.redactHeader("Authorization")
        logging.redactHeader("Cookie");

        return OkHttpClient.Builder()
            .addInterceptor(logging)
            .addNetworkInterceptor(AuthorizationNetworkInterceptor())
            .build()

    }


    @Provides
    fun provideRetrofit(client: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BuildConfig.BASE_API_URL)
            .client(client)
            .addConverterFactory(ScalarsConverterFactory.create())
            .build()
    }

    @Provides
    fun provideConverterService(retrofit: Retrofit): ConverterService {
        return retrofit
            .create(ConverterService::class.java)
    }
}