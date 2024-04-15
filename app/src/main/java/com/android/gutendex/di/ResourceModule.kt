package com.android.gutendex.di

import com.android.gutendex.helpers.ResourceProvider
import com.android.gutendex.helpers.ResourceProviderImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class ResourceModule {

    @Provides
    @Singleton
    fun provideResourceProvider(impl: ResourceProviderImpl): ResourceProvider = impl

    @Provides
    @Singleton
    fun provideCoroutineScopes(): CoroutineScope {
        return CoroutineScope(SupervisorJob())
    }
}