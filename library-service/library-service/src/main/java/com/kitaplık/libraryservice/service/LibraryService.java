package com.kitaplık.libraryservice.service;

import com.kitaplık.libraryservice.client.BookServiceClient;
import com.kitaplık.libraryservice.dto.AddBookRequest;
import com.kitaplık.libraryservice.dto.LibraryDto;
import com.kitaplık.libraryservice.exception.LibraryNotFoundException;
import com.kitaplık.libraryservice.model.Library;
import com.kitaplık.libraryservice.repository.LibraryRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

@Service
public class LibraryService {

    private final LibraryRepository libraryRepository;
    private final BookServiceClient bookServiceClient;

    public LibraryService(LibraryRepository libraryRepository, BookServiceClient bookServiceClient) {
        this.libraryRepository = libraryRepository;
        this.bookServiceClient = bookServiceClient;
    }


    public LibraryDto getAllBooksInLibraryById(String id){
        Library library = libraryRepository.findById(id)
                .orElseThrow(() -> new LibraryNotFoundException("Library not found with ID: " + id));

        return new LibraryDto(library.getId(),
                library.getUserBook().stream()
                        .map(book -> bookServiceClient.getBookById(book).getBody())
                        .collect(Collectors.toList()));
    }

    public LibraryDto createLibrary(){
        Library newLibrary = libraryRepository.save(new Library());
        return new LibraryDto(newLibrary.getId());
    }

    public void addBookToLibrary(AddBookRequest request){
        String bookId = bookServiceClient.getBookByIsbn(request.getIsbn()).getBody().getBookId();

        Library library = libraryRepository.findById(request.getId()).orElseThrow(
                () -> new LibraryNotFoundException("Library not found with ID: " + request.getId()));
        library.getUserBook().add(bookId);

        libraryRepository.save(library); //update
    }


}
