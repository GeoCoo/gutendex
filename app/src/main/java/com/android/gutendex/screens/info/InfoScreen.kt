package com.android.gutendex.screens.info

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.navigation.NavController
import com.android.gutendex.helpers.LifecycleEffect
import com.android.gutendex.helpers.LoadingIndicator
import com.android.gutendex.models.BookInfo

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
    Scaffold {
        if (state.value.errorMessage?.isNotEmpty() == true){
            Text(text = state.value.errorMessage.toString())
        }
        if (state.value.isLoading)
            LoadingIndicator()
        else
            BookInfo(state.value.bookInfo)

    }


}

@Composable
fun BookInfo(bookInfo: BookInfo?) {
    Card(
        colors = CardColors(
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
            .height(250.dp)
    ) {

        Column {
            Text(text = "Title: ${bookInfo?.title}")
            Text(text = "Author: ${bookInfo?.author}")
            Text(text = "AuthorBirth: ${bookInfo?.authorsBirth}")
            Text(text = "subject: ${bookInfo?.subject}")


        }
    }

}
