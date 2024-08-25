package com.kitaplık.bookservice.dto

import com.kitaplık.bookservice.model.Book
import jakarta.persistence.criteria.From

class BookIdDto @JvmOverloads constructor(
    val bookId: String? = "",
    val isbn: String? = ""
){
    companion object {
        @JvmStatic
        fun convert(from: Book): BookIdDto{
            return BookIdDto(
                from.id?.toString(),
                from.isbn)
        }

        @JvmStatic
        fun convertToBookIdDto(id: String, isbn: String): BookIdDto{
            return BookIdDto(id, isbn)
        }
    }
}