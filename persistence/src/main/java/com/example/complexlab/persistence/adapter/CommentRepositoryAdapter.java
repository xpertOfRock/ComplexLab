package com.example.complexlab.persistence.adapter;

import com.example.complexlab.core.model.Comment;
import com.example.complexlab.core.port.CommentRepositoryPort;
import com.example.complexlab.persistence.repository.CommentRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class CommentRepositoryAdapter implements CommentRepositoryPort {

    private final CommentRepository commentRepository;

    public CommentRepositoryAdapter(CommentRepository commentRepository) {
        this.commentRepository = commentRepository;
    }

    @Override
    public List<Comment> findByBookId(Long bookId) {
        return commentRepository.findByBookIdWithAuthor(bookId);
    }

    @Override
    public Comment save(Comment comment) {
        return commentRepository.save(comment);
    }

    @Override
    public Optional<Comment> findById(Long id) {
        return commentRepository.findById(id);
    }

    @Override
    public void delete(Comment comment) {
        commentRepository.delete(comment);
    }
}