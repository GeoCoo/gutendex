package com.android.gutendex.helpers

import com.android.gutendex.models.BookDomain
import com.android.gutendex.models.BookInfo

val mockBookInfo = BookInfo(
    "title", "author", "birth", "subject", "img"
)

val mockBooDkomain = BookDomain(
    title = "title", author = "author", id = 1, popularity = 10, img = "img"
)

val mockListBookDomain = listOf(
    BookDomain(
        title = "title", author = "author", id = 1, popularity = 10, img = "img"
    ),
    BookDomain(
        title = "title", author = "author", id = 2, popularity = 12, img = "img"
    ),
    BookDomain(
        title = "title", author = "author", id = 45, popularity = 55, img = "img"
    )

)