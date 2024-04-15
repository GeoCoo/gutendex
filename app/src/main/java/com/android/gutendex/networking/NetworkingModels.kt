package com.android.gutendex.networking

import com.google.gson.annotations.SerializedName

data class BooksResponseDto(
    val count: Int,
    val next: String,
    val previous: Any,
    val results: List<BookResults>
)

data class BookResults(
    val authors: List<Author>,
    val bookshelves: List<String>,
    val copyright: Boolean,
    val download_count: Int,
    val formats: Formats,
    val id: Int,
    val languages: List<String>,
    val media_type: String,
    val subjects: List<String>,
    val title: String,
    val translators: List<Translator>
)

data class Author(
    val birth_year: Int,
    val death_year: Int,
    val name: String
)

data class Formats(
    @SerializedName("text/html") val txtHmtl: String,
    @SerializedName("application/epub+zip") val applicationEpub: String,
    @SerializedName("application/x-mobipocket-ebook") val applicationEbook: String,
    @SerializedName("application/rdf+xml") val applicationRdf: String,
    @SerializedName("image/jpeg") val image: String,
    @SerializedName("application/octet-stream") val applicationStream: String,
    @SerializedName("text/plain; charset=us-ascii") val textPlain: String
)

data class Translator(
    val birth_year: Int,
    val death_year: Int,
    val name: String
)

data class SingeBookResponseDto(
    val authors: List<Author>,
    val bookshelves: List<String>,
    val copyright: Boolean,
    val download_count: Int,
    val formats: Formats,
    val id: Int,
    val languages: List<String>,
    val media_type: String,
    val subjects: List<String>,
    val title: String,
    val translators: List<Any>
)



