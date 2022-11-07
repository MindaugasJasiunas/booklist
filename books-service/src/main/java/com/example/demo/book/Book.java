package com.example.demo.book;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder

@Document
public class Book {
    @Id
    private String id;
    private String author;
    private String title;
    @Indexed(unique = true)
    private String ISBN;
    private int pages;
    private boolean hardTop;
    private boolean eBook;
    private String imageUrl;
}
