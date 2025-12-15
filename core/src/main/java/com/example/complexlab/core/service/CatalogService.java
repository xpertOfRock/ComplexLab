package com.example.complexlab.core.service;

import com.example.complexlab.core.model.Book;
import com.example.complexlab.core.model.Page;
import com.example.complexlab.core.model.PageRequest;

import java.util.Optional;

public interface CatalogService {

    Page<Book> getBooks(String query, PageRequest pageRequest);

    Optional<Book> getBook(Long id);
}
