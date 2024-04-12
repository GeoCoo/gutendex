package com.android.gutendex.screens.books

import android.annotation.SuppressLint
import android.service.autofill.OnClickAction
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.navigation.NavController
import com.android.gutendex.helpers.LifecycleEffect
import com.android.gutendex.helpers.LoadingIndicator
import com.android.gutendex.models.BookListing
import com.android.gutendex.networking.Author

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun BooksScreen(navController: NavController) {
    val viewModel = hiltViewModel<BooksViewModel>()
    val lifecycleOwner = LocalLifecycleOwner.current
    val state = viewModel.viewState

    LifecycleEffect(
        lifecycleOwner = LocalLifecycleOwner.current, lifecycleEvent = Lifecycle.Event.ON_RESUME
    ) {
        viewModel.setEvent(Event.GetBooks)
    }
    Scaffold {
        if (state.value.isLoading)
            LoadingIndicator()
        else
            BooksList(books = state.value.listOfBooks, onClickAction = {
                viewModel.setEvent(Event.NavigateToInfo(it,navController))
            })
    }

}


@Composable
fun BooksList(books: List<BookListing>?, onClickAction: (Int) -> Unit) {
    LazyColumn {
        books?.size?.let {
            items(it) { item ->
                BookItem(books[item], onClickAction)
            }
        }
    }
}

@Composable
fun BookItem(book: BookListing, onClickAction: (Int) -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .clickable {
                onClickAction.invoke(book.id)
            }
    ) {
        Text(text = "Title: ${book.title ?: ""}")
        Text(text = "Auhor: ${book.author ?: ""}")
    }

    HorizontalDivider(modifier = Modifier.fillMaxWidth())

}
