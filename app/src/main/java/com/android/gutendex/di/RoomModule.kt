package com.android.gutendex.di

import android.content.Context
import androidx.room.Room
import com.android.gutendex.room.BookDao
import com.android.gutendex.room.BookDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class RoomModule {

    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext appContext: Context): BookDatabase {
        return Room.databaseBuilder(
            appContext,
            BookDatabase::class.java,
            "Books"
        ).build()
    }

    @Provides
    fun provideChannelDao(appDatabase: BookDatabase): BookDao {
        return appDatabase.bookDao()
    }

}

