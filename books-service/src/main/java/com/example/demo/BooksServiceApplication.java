package com.example.demo;

import com.example.demo.auth.JwtTokenProvider;
import com.example.demo.book.Book;
import com.example.demo.book.BookRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Bean;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.util.Arrays;

@Slf4j

@EnableDiscoveryClient
@EnableConfigurationProperties({JwtTokenProvider.class})
@SpringBootApplication
public class BooksServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(BooksServiceApplication.class, args);
    }

    @Bean
    CommandLineRunner saveFromJSONToDB(BookRepository repo) {
        return args -> {
            if(repo!=null || repo.count().block() > 1) return;
            ObjectMapper mapper = new ObjectMapper();
            File file = ResourceUtils.getFile("classpath:books.json");
            Book[] books = mapper.readValue(file, Book[].class);
            Arrays.stream(books).forEach(book -> repo.save(book).subscribe());
            log.debug(books.length+" documents saved from 'books' JSON file to Mongo DB");
        };
    }
}
