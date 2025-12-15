package com.example.complexlab.core.port;

import com.example.complexlab.core.model.Comment;

import java.util.List;
import java.util.Optional;

public interface CommentRepositoryPort {
    List<Comment> findByBookId(Long bookId);

    Comment save(Comment comment);

    Optional<Comment> findById(Long id);

    void delete(Comment comment);
}
