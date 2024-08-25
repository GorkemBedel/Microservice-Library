package com.kitaplık.bookservice.dto

import com.kitaplık.bookservice.model.Book

class BookDto @JvmOverloads constructor(
    val id: BookIdDto? = null,
    val title: String,
    val bookYear: Int,
    val author: String,
    val pressName: String,
) {
    companion object {
        @JvmStatic
        fun convert(from: Book): BookDto {
            return BookDto(
                from.id?.let { BookIdDto.convertToBookIdDto(it, from.isbn) },
                from.title,
                from.bookYear,
                from.author,
                from.pressName
            )
        }
    }
}