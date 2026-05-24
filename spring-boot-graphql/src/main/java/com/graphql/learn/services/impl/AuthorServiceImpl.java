package com.graphql.learn.services.impl;

import com.graphql.learn.entities.Author;
import com.graphql.learn.repositories.AuthorRepo;
import com.graphql.learn.services.AuthorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AuthorServiceImpl implements AuthorService {

    private AuthorRepo authorRepo;

    @Autowired
    public AuthorServiceImpl(AuthorRepo authorRepo) {
        this.authorRepo = authorRepo;
    }

    @Override
    public Author create(Author author) {
        return this.authorRepo.save(author);
    }

    @Override
    public List<Author> getAll() {
        return this.authorRepo.findAll();
    }

    @Override
    public Author get(int authorId) {
        return this.authorRepo.findById(authorId).orElseThrow(() -> new RuntimeException("Author you are looking for not found on server !!"));
    }
}
