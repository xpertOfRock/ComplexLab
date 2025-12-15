package com.example.complexlab.web.api;

import com.example.complexlab.core.dto.BookDto;
import com.example.complexlab.core.model.Book;
import com.example.complexlab.core.model.Page;
import com.example.complexlab.core.model.PageRequest;
import com.example.complexlab.core.service.BookService;
import com.example.complexlab.core.service.CatalogService;
import jakarta.validation.constraints.NotBlank;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/books")
public class BookApiController {

    private final BookService bookService;
    private final CatalogService catalogService;

    public BookApiController(BookService bookService, CatalogService catalogService) {
        this.bookService = bookService;
        this.catalogService = catalogService;
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public Page<BookDto> all(
            @RequestParam(value = "q", required = false) String query,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size,
            @RequestParam(value = "sort", defaultValue = "-createdAt") String sort) {

        boolean ascending = true;
        String sortBy = sort;
        if (sort.startsWith("-")) {
            ascending = false;
            sortBy = sort.substring(1);
        } else if (sort.startsWith("+")) {
            sortBy = sort.substring(1);
        }
        PageRequest pageRequest = new PageRequest(page, size, sortBy, ascending);
        Page<Book> pageOfBooks = catalogService.getBooks(query, pageRequest);
        List<BookDto> dtoContent = pageOfBooks.getContent().stream().map(this::toDto).toList();
        return new Page<>(dtoContent, pageOfBooks.getPage(), pageOfBooks.getSize(), pageOfBooks.getTotalElements());
    }

    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public BookDto byId(@PathVariable Long id) {
        return catalogService.getBook(id).map(this::toDto).orElseThrow();
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public BookDto create(@RequestParam @NotBlank String title,
                          @RequestParam(required = false) String author,
                          @RequestParam(required = false) String description) {
        return toDto(bookService.create(title, author, description));
    }

    @PreAuthorize("isAuthenticated()")
    @PutMapping("/{id}")
    public BookDto update(@PathVariable Long id,
                          @RequestParam @NotBlank String title,
                          @RequestParam(required = false) String author,
                          @RequestParam(required = false) String description) {
        return toDto(bookService.update(id, title, author, description));
    }

    @PreAuthorize("isAuthenticated()")
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        bookService.delete(id);
    }

    private BookDto toDto(Book b) {
        return new BookDto(b.getId(), b.getTitle(), b.getAuthor(), b.getDescription(), b.getCreatedAt());
    }
}
