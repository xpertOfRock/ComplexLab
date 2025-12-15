package com.example.complexlab.core.port;

import com.example.complexlab.core.model.Book;
import com.example.complexlab.core.model.Page;
import com.example.complexlab.core.model.PageRequest;

import java.util.Optional;

public interface CatalogRepositoryPort {

    Page<Book> findAll(PageRequest pageRequest, String query);

    Optional<Book> findById(Long id);

    Book save(Book book);

    void deleteById(Long id);
}
