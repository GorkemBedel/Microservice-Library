package com.kitaplık.libraryservice.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kitaplık.libraryservice.client.BookServiceClient;
import com.kitaplık.libraryservice.dto.AddBookRequest;
import com.kitaplık.libraryservice.dto.BookDto;
import com.kitaplık.libraryservice.dto.BookIdDto;
import com.kitaplık.libraryservice.dto.LibraryDto;
import com.kitaplık.libraryservice.exception.LibraryNotFoundException;
import com.kitaplık.libraryservice.model.Library;
import com.kitaplık.libraryservice.repository.LibraryRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.internal.verification.Times;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.env.Environment;
import org.springframework.http.ResponseEntity;

import javax.annotation.meta.When;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class LibraryServiceTest {

    private LibraryService libraryService;

    private LibraryRepository libraryRepository;
    private  BookServiceClient bookServiceClient;
    private  Environment environment;
    private ObjectMapper mapper = new ObjectMapper();
    @Captor
    private ArgumentCaptor<Library> libraryServiceArgumentCaptor;

    @BeforeEach
    public void setUp(){
        libraryRepository = Mockito.mock(LibraryRepository.class);
        bookServiceClient = Mockito.mock(BookServiceClient.class);
        environment = Mockito.mock(Environment.class);

        libraryService = new LibraryService(libraryRepository, bookServiceClient, environment);
    }
    

    @DisplayName("should return Library Dto with detailed BookList with BookDto and updated LibraryId when the parameter of the getAllBooksInLibraryById library id exist and library UserBook list size more than 2")
    @Test
    // Adim 1: Test ismini yaz
    void shouldReturnDetailedBookListWithBookDtoAndUpdatedLibraryId_whenLibraryIdExistAndLibraryUserBookListSizeMoreThan2(){
        // Adim 2: Test verilerinin hazırlanması
        String id = "libraryId";
        List<String> userBook = Arrays.asList("bookId1", "bookId2", "bookId3");
        Library library = new Library(id, userBook);

        BookDto bookDto1 = new BookDto(new BookIdDto("bookId1", "isbn1"), "title1", 2021, "author1", "pressName1");
        BookDto bookDto2 = new BookDto(new BookIdDto("bookId2", "isbn1"), "title1", 2021, "author1", "pressName1");
        BookDto bookDto3 = new BookDto(new BookIdDto("bookId3", "isbn1"), "title1", 2021, "author1", "pressName1");

        List<BookDto> bookDtoList = Arrays.asList(bookDto1, bookDto2, bookDto3);

        LibraryDto expectedResult = new LibraryDto(id+3, bookDtoList);

        //Adim 3: Bağımlı servislerin davranışlarının belirlenmesi
        Mockito.when(libraryRepository.findById(id)).thenReturn(Optional.of(library));
        Mockito.when(bookServiceClient.getBookById("bookId1")).thenReturn(ResponseEntity.ok(bookDto1));
        Mockito.when(bookServiceClient.getBookById("bookId2")).thenReturn(ResponseEntity.ok(bookDto2));
        Mockito.when(bookServiceClient.getBookById("bookId3")).thenReturn(ResponseEntity.ok(bookDto3));

        //Adim 4: Test edilecek methodun çalıştırılması
        LibraryDto result = libraryService.getAllBooksInLibraryById(id);

        //Adım 5: Test sonucunun dogrulunmasi
        assertEquals(expectedResult, result);

        //Adim 6: Bağımlı servislerin çalıştırıldığının kontrol edilmesi
        Mockito.verify(libraryRepository).findById(id);
        Mockito.verify(bookServiceClient).getBookById("bookId1");
        Mockito.verify(bookServiceClient).getBookById("bookId2");
        Mockito.verify(bookServiceClient).getBookById("bookId3");
        Mockito.verify(bookServiceClient,new Times((3))).getBookById(Mockito.any(String.class));
    }


    @DisplayName("should return Library Dto with detailed BookList with BookDto and when the parameter of the getAllBooksInLibraryById library id exist")
    @Test
        // Adim 1: Test ismini yaz
    void shouldReturnDetailedBookListWithBookDto_whenLibraryIdExist(){
        // Adim 2: Test verilerinin hazırlanması
        String id = "libraryId";
        List<String> userBook = Arrays.asList("bookId1", "bookId2");
        Library library = new Library(id, userBook);

        BookDto bookDto1 = new BookDto(new BookIdDto("bookId1", "isbn1"), "title1", 2021, "author1", "pressName1");
        BookDto bookDto2 = new BookDto(new BookIdDto("bookId2", "isbn1"), "title1", 2021, "author1", "pressName1");

        List<BookDto> bookDtoList = Arrays.asList(bookDto1, bookDto2);

        LibraryDto expectedResult = new LibraryDto(id, bookDtoList);

        //Adim 3: Bağımlı servislerin davranışlarının belirlenmesi
        Mockito.when(libraryRepository.findById(id)).thenReturn(Optional.of(library));
        Mockito.when(bookServiceClient.getBookById("bookId1")).thenReturn(ResponseEntity.ok(bookDto1));
        Mockito.when(bookServiceClient.getBookById("bookId2")).thenReturn(ResponseEntity.ok(bookDto2));

        //Adim 4: Test edilecek methodun çalıştırılması
        LibraryDto result = libraryService.getAllBooksInLibraryById(id);

        //Adım 5: Test sonucunun dogrulunmasi
        assertEquals(expectedResult, result);

        //Adim 6: Bağımlı servislerin çalıştırıldığının kontrol edilmesi
        Mockito.verify(libraryRepository).findById(id);
        Mockito.verify(bookServiceClient).getBookById("bookId1");
        Mockito.verify(bookServiceClient).getBookById("bookId2");
        Mockito.verify(bookServiceClient,new Times((2))).getBookById(Mockito.any(String.class));
    }


    @DisplayName("should throw LibraryNotFoundException when the parameter of  getAllBooksInLibraryById library id does not exist")
    @Test
        // Adim 1: Test ismini yaz
    void shouldThrowLibraryNotFoundException_whenLibraryIdDoesNotExist(){
        // Adim 2: Test verilerinin hazırlanması
        String id = "libraryId";

        //Adim 3: Bağımlı servislerin davranışlarının belirlenmesi
        Mockito.when(libraryRepository.findById(id)).thenReturn(Optional.empty());

        //Adim 4: Test edilecek methodun çalıştırılması

        //Adım 5: Test sonucunun dogrulunmasi
        Assertions.assertThatThrownBy(() -> libraryService.getAllBooksInLibraryById(id))
                .isInstanceOf(LibraryNotFoundException.class)
                .hasMessageContaining("Library not found with ID: " + id);

        //assertThrows(LibraryNotFoundException.class, () -> libraryService.getAllBooksInLibraryById(id));

        //Adim 6: Bağımlı servislerin çalıştırıldığının kontrol edilmesi
        Mockito.verify(libraryRepository).findById(id);
        Mockito.verifyNoInteractions(bookServiceClient);
    }

    @Test
        // Adim 1: Test ismini yaz
    void shouldAddBookToLibrary_whenLibraryWithIdInAddBookRequestExistsAndBookWithIsbnInAddBookRequestExists() {
        // Adim 2: Test verilerinin hazırlanması
        String id = "libraryId";
        List<String> userBook = new ArrayList<>();
        Collections.addAll(userBook, "bookId1", "bookId2", "bookId3");
        Library library = new Library(id, userBook);

        String isbn = "isbnTest";
        AddBookRequest addBookRequest = new AddBookRequest(id, isbn);

        //Adim 3: Bağımlı servislerin davranışlarının belirlenmesi
        Mockito.when(bookServiceClient.getBookByIsbn(isbn)).thenReturn(ResponseEntity.ok(new BookIdDto("bookIdTest", "isbnTest" )));
        Mockito.when(libraryRepository.findById(addBookRequest.getId())).thenReturn(Optional.of(library));

        //Adim 4: Test edilecek methodun çalıştırılması
        libraryService.addBookToLibrary(addBookRequest);

        //Adım 5: Test sonucunun dogrulunmasi
        Mockito.verify(libraryRepository).save(libraryServiceArgumentCaptor.capture());
        Library capturedLibrary = libraryServiceArgumentCaptor.getValue();
        assertEquals(library, capturedLibrary);
        assertTrue(library.getUserBook().contains("bookIdTest"));

        //Adim 6: Bağımlı servislerin çalıştırıldığının kontrol edilmesi
        Mockito.verify(bookServiceClient).getBookByIsbn(isbn);
        Mockito.verify(libraryRepository).findById(addBookRequest.getId());
        Mockito.verify(bookServiceClient,new Times((1))).getBookByIsbn(Mockito.any(String.class));
        Mockito.verify(libraryRepository,new Times((1))).findById(Mockito.any(String.class));
        Mockito.verify(libraryRepository,new Times((1))).save(Mockito.any(Library.class));

    }


    @Test
        // Adim 1: Test ismini yaz
    void shouldAddDefaultBookToLibrary_whenLibraryWithIdInAddBookRequestExistsAndBookWithIsbnInAddBookRequestDoesNotExists() {
        // Adim 2: Test verilerinin hazırlanması
        String id = "libraryId";
        List<String> userBook = new ArrayList<>();
        Collections.addAll(userBook, "bookId1", "bookId2", "bookId3");
        Library library = new Library(id, userBook);

        String isbn = "isbnDoesNotExist";
        AddBookRequest addBookRequest = new AddBookRequest(id, isbn);

        //Adim 3: Bağımlı servislerin davranışlarının belirlenmesi
        Mockito.when(bookServiceClient.getBookByIsbn(isbn)).thenReturn(ResponseEntity.ok(new BookIdDto("default-bookId", isbn )));
        Mockito.when(libraryRepository.findById(addBookRequest.getId())).thenReturn(Optional.of(library));

        //Adim 4: Test edilecek methodun çalıştırılması
        libraryService.addBookToLibrary(addBookRequest);

        //Adım 5: Test sonucunun dogrulunmasi
        Mockito.verify(libraryRepository).save(libraryServiceArgumentCaptor.capture());
        Library capturedLibrary = libraryServiceArgumentCaptor.getValue();
        assertEquals(library, capturedLibrary);
        assertTrue(library.getUserBook().contains("default-bookId"));


        //Adim 6: Bağımlı servislerin çalıştırıldığının kontrol edilmesi
        Mockito.verify(bookServiceClient).getBookByIsbn(isbn);
        Mockito.verify(libraryRepository).findById(addBookRequest.getId());
        Mockito.verify(bookServiceClient,new Times((1))).getBookByIsbn(Mockito.any(String.class));
        Mockito.verify(libraryRepository,new Times((1))).findById(Mockito.any(String.class));
        Mockito.verify(libraryRepository,new Times((1))).save(Mockito.any(Library.class));
    }

    @Test
        // Adim 1: Test ismini yaz
    void shouldThrowLibraryNotFoundException_whenLibraryWithIdInAddBookRequestDoesNotExists() {
        // Adim 2: Test verilerinin hazırlanması
        String id = "libraryId";
        String isbn = "isbn";
        AddBookRequest addBookRequest = new AddBookRequest(id, isbn);

        //Adim 3: Bağımlı servislerin davranışlarının belirlenmesi
        Mockito.when(libraryRepository.findById(id)).thenReturn(Optional.empty());

        //Adim 4: Test edilecek methodun çalıştırılması

        //Adım 5: Test sonucunun dogrulunmasi
        Assertions.assertThatThrownBy(() -> libraryService.addBookToLibrary(addBookRequest))
                .isInstanceOf(LibraryNotFoundException.class)
                .hasMessageContaining("Library not found with ID: " + id);
        //assertThrows(LibraryNotFoundException.class, () -> libraryService.getAllBooksInLibraryById(id));

        //Adim 6: Bağımlı servislerin çalıştırıldığının kontrol edilmesi
        Mockito.verify(libraryRepository).findById(id);
        Mockito.verifyNoInteractions(bookServiceClient);
    }

    @Test
        // Adim 1: Test ismini yaz
    void shouldReturnLibraryDtoAndCreateALibraryWithGeneratedValueIdAndEmptyUserBook(){
        // Adim 2: Test verilerinin hazırlanması
        Library library = new Library(UUID.randomUUID().toString(), new ArrayList<>());
        LibraryDto expectedLibraryDto = new LibraryDto(library.getId(), List.of(),"8080");

        //Adim 3: Bağımlı servislerin davranışlarının belirlenmesi
        Mockito.when(libraryRepository.save(Mockito.any(Library.class))).thenReturn(library);
        Mockito.when(environment.getProperty("local.server.port")).thenReturn("8080");

        //Adim 4: Test edilecek methodun çalıştırılması,
        LibraryDto libraryDto = libraryService.createLibrary();

        //Adım 5: Test sonucunun dogrulunmasi
        assertEquals(expectedLibraryDto, libraryDto);

        //Adim 6: Bağımlı servislerin çalıştırıldığının kontrol edilmesi
        Mockito.verify(libraryRepository, new Times(1)).save(Mockito.any(Library.class));
        Mockito.verifyNoInteractions(bookServiceClient);
    }


    @AfterEach
    void tearDown() {
    }

}