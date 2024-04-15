package com.android.gutendex.screens.info

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import coil.request.CachePolicy
import coil.request.ImageRequest
import com.android.gutendex.R
import com.android.gutendex.helpers.LifecycleEffect
import com.android.gutendex.helpers.LoadingIndicatorFull
import com.android.gutendex.models.BookInfo

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun InfoScreen(navController: NavController, bookId: String) {
    val viewModel = hiltViewModel<InfoViewModel>()
    val lifecycleOwner = LocalLifecycleOwner.current
    val state = viewModel.viewState

    LifecycleEffect(
        lifecycleOwner = LocalLifecycleOwner.current, lifecycleEvent = Lifecycle.Event.ON_RESUME
    ) {
        viewModel.setEvent(Event.GetBookInfo(bookId))
    }
    Scaffold(topBar = {
        TopAppBar(colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer,
            titleContentColor = MaterialTheme.colorScheme.surfaceTint
        ), title = {
            state.value.bookInfo?.title?.let {
                Text(
                    text = it, maxLines = 1, overflow = TextOverflow.Ellipsis
                )
            }
        }, navigationIcon = {
            IconButton(onClick = { navController.popBackStack() }) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "",
                    tint = MaterialTheme.colorScheme.surfaceTint
                )
            }
        })
    }) { paddongValues ->
        Column(
            modifier = Modifier
                .padding(paddongValues)
                .fillMaxSize()
        ) {
            if (state.value.errorMessage?.isNotEmpty() == true) {
                Text(text = state.value.errorMessage.toString())
            }
            if (state.value.isLoading) {
                LoadingIndicatorFull()
            } else BookInfo(state.value.bookInfo)
        }


    }


}

@Composable
fun BookInfo(bookInfo: BookInfo?) {
    Box(
        modifier = Modifier
            .padding(top = 16.dp, bottom = 8.dp, start = 16.dp, end = 16.dp)
            .fillMaxWidth()
            .height(250.dp)
    ) {
        Row {
            Column {
                bookInfo?.img?.let {
                    val builder =
                        ImageRequest.Builder(LocalContext.current).data(it).memoryCacheKey(it)
                            .diskCacheKey(it).diskCachePolicy(CachePolicy.ENABLED)
                            .memoryCachePolicy(CachePolicy.ENABLED)
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
            Column(modifier = Modifier.padding(start = 16.dp)) {
                Text(text = "Title: ${bookInfo?.title}")
                Text(text = "Author: ${bookInfo?.author}")
                Text(text = "AuthorBirth: ${bookInfo?.authorsBirth}")
                Text(text = "subject: ${bookInfo?.subject}")


            }
        }
    }

}
