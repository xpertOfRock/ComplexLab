package com.example.complexlab.persistence.adapter;

import com.example.complexlab.core.model.Book;
import com.example.complexlab.core.model.Page;
import com.example.complexlab.core.model.PageRequest;
import com.example.complexlab.core.port.*;
import com.example.complexlab.persistence.repository.BookRepository;
import org.springframework.stereotype.Repository;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
public class CatalogRepositoryAdapter implements CatalogRepositoryPort {

    private final BookRepository bookRepository;

    public CatalogRepositoryAdapter(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    @Override
    public Page<Book> findAll(PageRequest pageRequest, String query) {
        List<Book> allBooks = bookRepository.findAll();
        List<Book> filtered = allBooks.stream()
                .filter(book -> {
                    if (query == null || query.isBlank()) {
                        return true;
                    }
                    String q = query.toLowerCase();
                    String title = book.getTitle() != null ? book.getTitle().toLowerCase() : "";
                    String author = book.getAuthor() != null ? book.getAuthor().toLowerCase() : "";
                    return title.contains(q) || author.contains(q);
                })
                .collect(Collectors.toList());

        Comparator<Book> comparator;
        String sortBy = pageRequest.getSortBy();
        boolean asc = pageRequest.isAscending();
        if ("title".equalsIgnoreCase(sortBy)) {
            comparator = Comparator.comparing(Book::getTitle, Comparator.nullsLast(String::compareToIgnoreCase));
        } else if ("author".equalsIgnoreCase(sortBy)) {
            comparator = Comparator.comparing(Book::getAuthor, Comparator.nullsLast(String::compareToIgnoreCase));
        } else {
            comparator = Comparator.comparing(Book::getCreatedAt);
        }
        if (!asc) {
            comparator = comparator.reversed();
        }
        filtered.sort(comparator);

        int fromIndex = Math.min(pageRequest.getPage() * pageRequest.getSize(), filtered.size());
        int toIndex = Math.min(fromIndex + pageRequest.getSize(), filtered.size());
        List<Book> content = filtered.subList(fromIndex, toIndex);
        return new Page<>(content, pageRequest.getPage(), pageRequest.getSize(), filtered.size());
    }

    @Override
    public Optional<Book> findById(Long id) {
        return bookRepository.findById(id);
    }

    @Override
    public Book save(Book book) {
        return bookRepository.save(book);
    }

    @Override
    public void deleteById(Long id) {
        bookRepository.deleteById(id);
    }
}
