package com.example.demo;

import com.example.demo.book.Book;

import java.util.List;

public class HelperClass {
    public static List<Book> getFakeBooksList(){
        return List.of(
                Book.builder().id("abc").title("Title1").author("Author1").ISBN("123456789").pages(123).imageUrl("https://img1").isEBook(true).isHardTop(false).build(),
                Book.builder().id("def").title("Title2").author("Author2").ISBN("987654321").pages(321).imageUrl("https://img2").isEBook(false).isHardTop(true).build()
        );
    }
}
