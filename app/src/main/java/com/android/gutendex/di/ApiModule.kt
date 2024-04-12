package com.android.gutendex.di

import com.android.gutendex.networking.ApiClient
import com.android.gutendex.networking.ApiClientImpl
import com.android.gutendex.networking.ApiService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class ApiModule {

    @Provides
    @Singleton
    fun provideApiService(retrofit: Retrofit): ApiService = retrofit.create(ApiService::class.java)

    @Provides
    @Singleton
    fun provideApiClient(impl: ApiClientImpl): ApiClient = impl
}