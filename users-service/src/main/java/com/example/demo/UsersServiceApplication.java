package com.example.demo;

import com.example.demo.authority.Authority;
import com.example.demo.role.Role;
import com.example.demo.security.JwtTokenProvider;
import com.example.demo.user.User;
import com.example.demo.user.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@EnableDiscoveryClient
@EnableConfigurationProperties({JwtTokenProvider.class})
@SpringBootApplication
public class UsersServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(UsersServiceApplication.class, args);
    }

    @Bean
    CommandLineRunner initDatabase(UserRepository repository, PasswordEncoder encoder) {
        return args -> {
            repository.count().flatMap(number -> {
                if(number == 0){
                    var bookReadAuthority = Authority.builder()
                            .authorityName("book:read")
                            .build();
                    var bookCreateAuthority = Authority.builder()
                            .authorityName("book:create")
                            .build();
                    var bookUpdateAuthority = Authority.builder()
                            .authorityName("book:update")
                            .build();
                    var bookDeleteAuthority = Authority.builder()
                            .authorityName("book:delete")
                            .build();

                    var booklisterRole = Role.builder()
                            .roleName("booklister")
                            .authorities(List.of(bookReadAuthority))
                            .build();
                    var librarianRole = Role.builder()
                            .roleName("booklister")
                            .authorities(List.of(bookReadAuthority, bookCreateAuthority, bookUpdateAuthority, bookDeleteAuthority))
                            .build();

                    var user = User.builder()
                            .publicId(UUID.randomUUID())
                            .email("john.doe@example.com")
                            .firstName("John")
                            .lastName("Doe")
                            .password(encoder.encode("password"))
                            .role(booklisterRole)
                            .createdDate(LocalDateTime.now())
                            .lastModifiedDate(LocalDateTime.now())
                            .isAccountNonExpired(true)
                            .isAccountNonLocked(true)
                            .isCredentialsNonExpired(true)
                            .isEnabled(true)
                            .build();
                    var librarian = User.builder()
                            .publicId(UUID.randomUUID())
                            .email("jane.doe@example.com")
                            .firstName("Jane")
                            .lastName("Doe")
                            .password(encoder.encode("password"))
                            .role(librarianRole)
                            .createdDate(LocalDateTime.now())
                            .lastModifiedDate(LocalDateTime.now())
                            .isAccountNonExpired(true)
                            .isAccountNonLocked(true)
                            .isCredentialsNonExpired(true)
                            .isEnabled(true)
                            .build();

                    return repository.save(user)
                            .flatMap(savedUser -> repository.save(librarian));
                }else{
                    return Mono.empty();
                }
            }).subscribe();
        };
    }
}
