package com.reactive.app.repositories;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import reactor.test.StepVerifier;

@SpringBootTest
public class RepositoryTest {

    @Autowired
    private BookRepository bookRepository;

    @Test
    public void testFindMethods() {
        bookRepository.findByName("Swami and Friends")
                .as(StepVerifier::create)
                .expectNextCount(1)
                .verifyComplete();

        bookRepository.findByAuthor("RK Narayan")
                .as(StepVerifier::create)
                .expectNextCount(1)
                .verifyComplete();

        bookRepository.findByNameAndAuthor("Swami and Friends", "RK Narayan")
                .as(StepVerifier::create)
                .expectNextCount(1)
                .verifyComplete();

        bookRepository.findByPublisher("TechPress")
                .as(StepVerifier::create)
                .expectNextCount(1)
                .verifyComplete();
    }

    @Test
    public void testSearchMethods() {
        bookRepository.searchBookByTitle("%and%")
                .as(StepVerifier::create)
                .expectNextCount(1)
                .verifyComplete();
    }

}
