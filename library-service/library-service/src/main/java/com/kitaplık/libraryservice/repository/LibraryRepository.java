package com.kitaplık.libraryservice.repository;

import com.kitaplık.libraryservice.model.Library;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LibraryRepository extends JpaRepository<Library,String> {
}
