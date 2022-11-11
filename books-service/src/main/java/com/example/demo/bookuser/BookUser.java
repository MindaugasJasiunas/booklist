package com.example.demo.bookuser;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@AllArgsConstructor
@Data

@Document(collection = "books_users")
public class BookUser {
    @Id
    private String id;
    private String userEmail;
    private String bookISBN;
    private boolean isWishlist = false;

}
