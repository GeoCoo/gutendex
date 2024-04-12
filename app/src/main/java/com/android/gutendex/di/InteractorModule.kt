package com.android.gutendex.di

import com.android.gutendex.screens.books.BookInteractorImpl
import com.android.gutendex.screens.books.BooksInteractor
import com.android.gutendex.screens.info.InfoInteractor
import com.android.gutendex.screens.info.InfoInteractorImpl
import com.android.gutendex.screens.main.MainInteractor
import com.android.gutendex.screens.main.MainInteractorImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
class InteractorModule {

    @Provides
    fun provideMainInteractor(impl: MainInteractorImpl): MainInteractor = impl


    @Provides
    fun provideBooksInteractor(impl: BookInteractorImpl): BooksInteractor = impl

    @Provides
    fun provideBookInfoInteractor(impl: InfoInteractorImpl): InfoInteractor = impl
}