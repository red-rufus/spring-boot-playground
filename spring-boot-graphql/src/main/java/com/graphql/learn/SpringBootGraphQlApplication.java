package com.graphql.learn;

import com.graphql.learn.entities.Author;
import com.graphql.learn.entities.Book;
import com.graphql.learn.services.AuthorService;
import com.graphql.learn.services.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SpringBootGraphQlApplication implements CommandLineRunner {

    @Autowired
    private BookService bookService;

    @Autowired
    private AuthorService authorService;

    public static void main(String[] args) {
        SpringApplication.run(SpringBootGraphQlApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {

        Author a1 = this.authorService.create(new Author("Munshi Premchand"));
        Author a2 = this.authorService.create(new Author("R.K. Narayan"));
        Author a3 = this.authorService.create(new Author("Rabindranath Tagore"));

        Book b1 = new Book("Gitanjali", 250.00, 100, a3);
        Book b2 = new Book("Malgudi Days",  500.00, 200, a2);
        Book b3 = new Book("Karmabhoomi", 300.00, 300, a1);
        Book b4 = new Book("Swami and Friends", 350.00, 150, a2);

        this.bookService.create(b1);
        this.bookService.create(b2);
        this.bookService.create(b3);
        this.bookService.create(b4);
    }
}
