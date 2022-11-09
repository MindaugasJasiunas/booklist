package com.example.demo.user;

import com.example.demo.security.AuthRequest;
import org.springframework.security.core.userdetails.UserDetails;
import reactor.core.publisher.Mono;

public interface UserService {
    Mono<User> register(User user);
    Mono<UserDetails> login(AuthRequest request);
}
