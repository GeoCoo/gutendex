package com.android.gutendex.screens.books

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import coil.request.CachePolicy.ENABLED
import coil.request.ImageRequest
import com.android.gutendex.R
import com.android.gutendex.helpers.LifecycleEffect
import com.android.gutendex.helpers.LoadingIndicator
import com.android.gutendex.models.BookDomain

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun BooksScreen(navController: NavController) {
    val viewModel = hiltViewModel<BooksViewModel>()
    val lifecycleOwner = LocalLifecycleOwner.current
    val state = viewModel.viewState
    val lazyColumnListState = rememberLazyListState()

    LifecycleEffect(
        lifecycleOwner = LocalLifecycleOwner.current, lifecycleEvent = Lifecycle.Event.ON_CREATE
    ) {
        viewModel.setEvent(Event.GetInitialBooks)
    }

    Scaffold(topBar = {
        TopAppBar(colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer,
            titleContentColor = MaterialTheme.colorScheme.surfaceTint
        ), title = { Text(text = "Gutendex") })
    }) { paddingValues ->
        if (state.value.isLoading == true) {
            Box(
                modifier = Modifier
                    .fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        }

        LazyColumn(
            state = lazyColumnListState,
            modifier = Modifier.padding(top = paddingValues.calculateTopPadding())
        ) {
            state.value.listOfBooks.let { books ->
                Log.d("BOOKS SIZW", books.size.toString())
                items(books.size) { index ->
                    BookItem(book = books[index]) { _ ->
                        viewModel.setEvent(Event.NavigateToInfo(books[index].id, navController))
                    }
                    state.value.totalBooksCount ?: state.value.booksDbCount?.let {
                        if (index == books.lastIndex && books.size < it && state.value.isLoadingNext.not()
                        ) {
                            viewModel.setEvent(Event.UpdatePage(state.value.pages))
                        }
                    }
                }
                if (state.value.isLoadingNext) {
                    item { LoadingIndicator() }
                }
            }
        }
    }

    LaunchedEffect(Unit) {
        viewModel.effect.flowWithLifecycle(lifecycleOwner.lifecycle, Lifecycle.State.RESUMED)
            .collect { effect ->
                when (effect) {
                    is Effect.SaveToDB -> {
                        viewModel.setEvent(Event.SaveToDb(effect.books))
                    }

                    is Effect.LoadBooks -> {
                        viewModel.setEvent(Event.LoadBooks(effect.page))
                    }

                    is Effect.GetMoreBooks -> {
                        viewModel.setEvent(Event.GetMoreBooks(effect.page))
                    }
                }
            }
    }
}

@Composable
fun BookItem(book: BookDomain, onClickAction: (Int) -> Unit) {
    Card(colors = CardColors(
        containerColor = MaterialTheme.colorScheme.primaryContainer,
        contentColor = MaterialTheme.colorScheme.surfaceTint,
        disabledContainerColor = MaterialTheme.colorScheme.primaryContainer,
        disabledContentColor = MaterialTheme.colorScheme.primaryContainer
    ),
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp),
        shape = RoundedCornerShape(6.dp),
        modifier = Modifier
            .padding(top = 8.dp, bottom = 8.dp, start = 16.dp, end = 16.dp)
            .fillMaxWidth()
            .height(100.dp)
            .clickable { onClickAction.invoke(book.id) }) {

        Row {
            Column {
                book.img?.let {
                    val builder = ImageRequest.Builder(LocalContext.current)
                        .data(it)
                        .memoryCacheKey(it)
                        .diskCacheKey(it)
                        .diskCachePolicy(ENABLED)
                        .memoryCachePolicy(ENABLED)
                        .placeholder(R.drawable.ic_launcher_foreground)

                    Image(
                        painter = rememberAsyncImagePainter(remember(it) {
                            builder.build()
                        }),
                        contentScale = ContentScale.Fit,
                        contentDescription = null,

                        )
                }

            }

            Column(modifier = Modifier.padding(16.dp)) {
                Text(text = "Title: ${book.title ?: ""}")
                Text(text = "Auhor: ${book.author ?: ""}")
            }
        }
    }

}
