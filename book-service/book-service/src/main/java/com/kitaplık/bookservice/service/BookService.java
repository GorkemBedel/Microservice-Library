package com.kitaplık.bookservice.service;

import com.kitaplık.bookservice.dto.BookDto;
import com.kitaplık.bookservice.dto.BookIdDto;
import com.kitaplık.bookservice.exception.BookNotFoundException;
import com.kitaplık.bookservice.repository.BookRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class BookService {

    private final BookRepository bookRepository;

    public BookService(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    public List<BookDto> getAllBooks() {
        return bookRepository.findAll()
                .stream()
                .map(BookDto::convert)
                .collect(Collectors.toList());
    }

    public BookIdDto findByIsbn(String isbn){
        return bookRepository.findByIsbn(isbn)
                .map(BookIdDto::convert)
                .orElseThrow(() -> new BookNotFoundException("Book not found with ISBN: " + isbn));
    }

    public BookDto findBookById(String id){
        return bookRepository.findById(id)
                .map(BookDto::convert)
                .orElseThrow(() -> new BookNotFoundException("Book not found with ID: " + id));
    }


 }
