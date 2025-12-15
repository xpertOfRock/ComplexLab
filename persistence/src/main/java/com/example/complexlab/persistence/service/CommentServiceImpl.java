package com.example.complexlab.persistence.service;

import com.example.complexlab.core.model.AppUser;
import com.example.complexlab.core.model.Book;
import com.example.complexlab.core.model.Comment;
import com.example.complexlab.core.service.CommentService;
import com.example.complexlab.persistence.repository.BookRepository;
import com.example.complexlab.persistence.repository.CommentRepository;
import com.example.complexlab.persistence.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.Instant;
import java.util.List;

@Service
public class CommentServiceImpl implements CommentService {

    private static final Logger log = LoggerFactory.getLogger(CommentServiceImpl.class);

    private final CommentRepository commentRepository;
    private final BookRepository bookRepository;
    private final UserRepository userRepository;

    public CommentServiceImpl(CommentRepository commentRepository, BookRepository bookRepository, UserRepository userRepository) {
        this.commentRepository = commentRepository;
        this.bookRepository = bookRepository;
        this.userRepository = userRepository;
    }

    @Override
    public List<Comment> findByBookId(Long bookId) {
        return commentRepository.findByBookIdWithAuthor(bookId);
    }

    @Override
    @Transactional
    public Comment addComment(Long bookId, String username, String text) {
        Book book = bookRepository.findById(bookId).orElseThrow(EntityNotFoundException::new);

        AppUser author = userRepository.findByUsername(username).orElseThrow(EntityNotFoundException::new);
        
        Comment comment = commentRepository.save(new Comment(book, author, text, Instant.now()));

        log.info("Created comment id={} bookId={} author={} createdAt={}", comment.getId(), bookId, username, comment.getCreatedAt());
        return comment;
    }

    @Override
    @Transactional
    public void delete(Long commentId, String username, boolean isAdmin) {
        Comment comment = commentRepository.findById(commentId).orElseThrow(EntityNotFoundException::new);
        String authorUsername = comment.getAuthor().getUsername();

        if (!isAdmin && !authorUsername.equals(username)) {
            throw new AccessDeniedException("Not allowed");
        }

        Instant createdAt = comment.getCreatedAt();
        Instant now = Instant.now();

        boolean within24h = Duration.between(createdAt, now).compareTo(Duration.ofHours(24)) <= 0;

        if (!isAdmin && !within24h) {
            throw new IllegalStateException("Comment can only be deleted within 24 hours");
        }
        commentRepository.delete(comment);
        log.info("Deleted comment id={} bookId={} author={} createdAt={}", comment.getId(), comment.getBook().getId(), authorUsername, comment.getCreatedAt());
    }
}
