package com.android.gutendex.di

import com.android.gutendex.room.RoomBookRepository
import com.android.gutendex.room.RoomBookRepositoryImpl
import com.android.gutendex.screens.books.BooksRepository
import com.android.gutendex.screens.books.BooksRepositoryImpl
import com.android.gutendex.screens.info.InfoRepository
import com.android.gutendex.screens.info.InfoRepositoryImpl

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
    fun provideBooksRepository(impl: BooksRepositoryImpl): BooksRepository = impl

    @Provides
    @Singleton
    fun provideRoomBooksRepository(impl: RoomBookRepositoryImpl): RoomBookRepository = impl

    @Provides
    @Singleton
    fun provideInfoRepository(impl: InfoRepositoryImpl): InfoRepository = impl


}





