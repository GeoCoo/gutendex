package com.android.gutendex.di

import com.android.gutendex.networking.baseUrl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
class NetworkModule {

    /**
     * Constructs and provides a singleton [Retrofit] instance used to perform api calls.
     */
    @Provides
    @Singleton
    fun provideRetrofit(
        okHttpClient: OkHttpClient,
        converterFactory: GsonConverterFactory,
    ): Retrofit = Retrofit.Builder()
        .addConverterFactory(converterFactory)
        .baseUrl(baseUrl)
        .client(okHttpClient)
        .build()


    /**
     * Constructs and provides a singleton [OkHttpClient] used for [Retrofit] instance
     * initialization.
     *
     * @return A [OkHttpClient] instance.
     */
    @Singleton
    @Provides
    fun provideOkHttpClient(
        httpLoggingInterceptor: HttpLoggingInterceptor,
    ): OkHttpClient {

        val client = OkHttpClient.Builder()
            .addInterceptor(httpLoggingInterceptor)
            .readTimeout(3000, TimeUnit.MILLISECONDS)
            .connectTimeout(3000, TimeUnit.MILLISECONDS)
            .writeTimeout(3000, TimeUnit.MILLISECONDS)

        return client.build()
    }

    @Singleton
    @Provides
    fun provideConverterFactory(): GsonConverterFactory = GsonConverterFactory.create()

    /** Constructs and provides a singleton [HttpLoggingInterceptor] instance.
     *
     * @return A [HttpLoggingInterceptor] instance.
     */
    @Singleton
    @Provides
    fun providesHttpLoggingInterceptor(
    ) = HttpLoggingInterceptor()
        .apply {
            level = HttpLoggingInterceptor.Level.BODY

        }


}