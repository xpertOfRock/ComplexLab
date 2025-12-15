package com.example.complexlab.persistence.service;

import com.example.complexlab.core.model.Book;
import com.example.complexlab.core.model.Page;
import com.example.complexlab.core.model.PageRequest;
import com.example.complexlab.core.port.CatalogRepositoryPort;
import com.example.complexlab.core.service.CatalogService;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CatalogServiceImpl implements CatalogService {

    private final CatalogRepositoryPort catalogRepositoryPort;

    public CatalogServiceImpl(CatalogRepositoryPort catalogRepositoryPort) {
        this.catalogRepositoryPort = catalogRepositoryPort;
    }

    @Override
    public Page<Book> getBooks(String query, PageRequest pageRequest) {
        return catalogRepositoryPort.findAll(pageRequest, query);
    }

    @Override
    public Optional<Book> getBook(Long id) {
        return catalogRepositoryPort.findById(id);
    }
}
