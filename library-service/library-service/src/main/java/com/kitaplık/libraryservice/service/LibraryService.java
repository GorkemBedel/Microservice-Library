package com.kitaplık.libraryservice.service;

import com.kitaplik.bookservice.BookId;
import com.kitaplik.bookservice.BookServiceGrpc;
import com.kitaplik.bookservice.Isbn;
import com.kitaplık.libraryservice.client.BookServiceClient;
import com.kitaplık.libraryservice.dto.AddBookRequest;
import com.kitaplık.libraryservice.dto.LibraryDto;
import com.kitaplık.libraryservice.exception.LibraryNotFoundException;
import com.kitaplık.libraryservice.model.Library;
import com.kitaplık.libraryservice.repository.LibraryRepository;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.core.env.Environment;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class LibraryService {

    private final LibraryRepository libraryRepository;
    private final BookServiceClient bookServiceClient;
    private final Environment environment;
    @GrpcClient("book-service")
    private BookServiceGrpc.BookServiceBlockingStub bookServiceBlockingStub;

    public LibraryService(LibraryRepository libraryRepository, BookServiceClient bookServiceClient, Environment environment) {
        this.libraryRepository = libraryRepository;
        this.bookServiceClient = bookServiceClient;
        this.environment = environment;
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
        return new LibraryDto(newLibrary.getId(), List.of(),environment.getProperty("local.server.port"));
    }

    public void addBookToLibrary(AddBookRequest request){
        BookId bookId = bookServiceBlockingStub.getBookIdByIsbn(Isbn.newBuilder().setIsbn(request.getIsbn()).build());
        // String bookId = bookServiceClient.getBookByIsbn(request.getIsbn()).getBody().getBookId();

        Library library = libraryRepository.findById(request.getId()).orElseThrow(
                () -> new LibraryNotFoundException("Library not found with ID: " + request.getId()));
        library.getUserBook().add(bookId.getBookId());

        libraryRepository.save(library); //update
    }


    public List<String> getAllLibraries() {
        return libraryRepository.findAll()
                .stream()
                .map(library -> library.getId())
                .collect(Collectors.toList());
    }
}
