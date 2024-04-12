package com.android.gutendex.di

import com.android.gutendex.screens.books.BooksRepository
import com.android.gutendex.screens.books.BooksRepositoryImpl
import com.android.gutendex.screens.info.InfoRepository
import com.android.gutendex.screens.info.InfoRepositoryImpl
import com.android.gutendex.screens.main.MainRepository
import com.android.gutendex.screens.main.MainRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class RepositoryModule {
    @Provides
    @Singleton
    fun provideMainRepository(impl: MainRepositoryImpl): MainRepository = impl

    @Provides
    @Singleton
    fun provideBooksRepository(impl: BooksRepositoryImpl): BooksRepository = impl

    @Provides
    @Singleton
    fun provideInfoRepository(impl: InfoRepositoryImpl): InfoRepository = impl

}





