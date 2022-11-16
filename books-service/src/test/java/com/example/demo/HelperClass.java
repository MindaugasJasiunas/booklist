package com.example.demo;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.example.demo.book.Book;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

@Component
public class HelperClass {
    private int tenMinutesInMiliseconds = 600000;
    @Value("${jwt.secretKey}")
    private String jwtSecret;

    public static List<Book> getFakeBooksList(){
        return List.of(
                Book.builder().id("abc").title("Title1").author("Author1").ISBN("123456789").pages(123).imageUrl("https://img1").isEBook(true).isHardTop(false).build(),
                Book.builder().id("def").title("Title2").author("Author2").ISBN("987654321").pages(321).imageUrl("https://img2").isEBook(false).isHardTop(true).build()
        );
    }

    public String generateJWTTokenWithAuthorities(String authority){
        return JWT.create()
                .withIssuer("AppName")
                .withAudience("AppName Administration")
                .withIssuedAt(new Date())
                .withSubject("TestUser")
                .withArrayClaim("authorities", new String[]{authority})
                .withExpiresAt(new Date(System.currentTimeMillis() + tenMinutesInMiliseconds))
                .sign(Algorithm.HMAC512(jwtSecret));
    }
}
