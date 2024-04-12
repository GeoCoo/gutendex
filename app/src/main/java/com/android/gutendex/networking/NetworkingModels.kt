package com.android.gutendex.networking

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
    val textHtml: String,
    val applicationEpubZip: String,
    val applicationXMobipocketEbook: String,
    val applicationRdfXml: String,
    val imageJpeg: String,
    val textPlainCharsetUsAscii: String,
    val applicationOctetStream: String,
    val textHtmlCharsetUtf8: String?,
    val textPlainCharsetUtf8: String?,
    val textPlainCharsetIso88591: String?,
    val textHtmlCharsetIso88591: String?,
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



