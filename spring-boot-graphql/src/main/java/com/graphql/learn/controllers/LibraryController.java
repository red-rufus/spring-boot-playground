package com.graphql.learn.controllers;

import com.graphql.learn.entities.Author;
import com.graphql.learn.entities.Book;
import com.graphql.learn.services.AuthorService;
import com.graphql.learn.services.BookService;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
public class LibraryController {

    @Autowired
    private BookService bookService;

    @Autowired
    private AuthorService authorService;

    //create
    @MutationMapping("createBook")
    public Book create(@Argument BookInput book) {
        Book b=new Book();
        b.setTitle(book.getTitle());
        b.setPrice(book.getPrice());
        b.setPages(book.getPages());
        b.setPages(book.getPages());

        Author a = new Author();
        a.setName(book.getAuthor().getName());
        this.authorService.create(a);

        b.setAuthor(a);
        return this.bookService.create(b);
    }

    //get all
    @QueryMapping("allBooks")
    public List<Book> getAll() {
        return this.bookService.getAll();
    }

    //get single book
    @QueryMapping("getBook")
    public Book get(@Argument int bookId) {
        return this.bookService.get(bookId);
    }

}

@Getter
@Setter
class BookInput{
    private String title;
    private String desc;
    private double price;
    private int pages;
    private AuthorInput author;
}

@Getter
@Setter
class AuthorInput{
    private String name;
}
