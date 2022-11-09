package com.example.demo.book;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder

@Document(collection = "books")
public class Book {
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Id
    private String id;
    private String author;
    private String title;
    @Indexed(unique = true)
    private String ISBN;
    private int pages;
    private boolean isHardTop;
    private boolean isEBook;
    private String imageUrl;
}
