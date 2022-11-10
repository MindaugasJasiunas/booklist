package com.example.demo.user;

import com.example.demo.authority.Authority;
import com.example.demo.role.Role;
//import com.example.demo.role.RoleRepository;
import com.example.demo.requests.AuthRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor

@Service
public class UserServiceImpl implements UserService, ReactiveUserDetailsService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public Mono<User> register(User user){
        // default roleId of ROLE_USER (if no role provided)
        if(user.getRole() == null) user.setRole(getDefaultRole());

        UserValidator.ValidationResult result = isUserValid(user);
        if(result != UserValidator.ValidationResult.SUCCESS){
            return Mono.error(() -> new RuntimeException("Submitted user is not valid: "+result.getReason()));
        }

        Mono<User> userMono = Mono.just(user);
        return userMono
                .filterWhen(this::userNotExistsInDBByEmail)
                .switchIfEmpty(Mono.error(() -> new RuntimeException("User with provided email already exists")))
//                .zipWith(roleRepository.findById(user.getRoleId()))
//                .switchIfEmpty(Mono.error(() -> new RuntimeException("Role provided does not exist")))
//                .map(tuple -> {
//                    tuple.getT1().setRoleId(tuple.getT2().getId());
//                    return tuple.getT1();
//                })
                .map(userToSave -> {
                    userToSave.setPublicId(UUID.randomUUID());
                    userToSave.setPassword(passwordEncoder.encode(userToSave.getPassword()));
                    userToSave.setCreatedDate(LocalDateTime.now());
                    userToSave.setLastModifiedDate(LocalDateTime.now());
                    userToSave.setAccountNonLocked(true);
                    userToSave.setAccountNonExpired(true);
                    userToSave.setCredentialsNonExpired(true);
                    userToSave.setEnabled(true);
                    return userToSave;
                })
                .flatMap(userRepository::save)
//                .flatMap(this::populateUserWithRolesAndAuthorities)
                .switchIfEmpty(Mono.error(() -> new RuntimeException("User with provided username already exists")) );
    }

    @Override
    public Mono<UserDetails> login(AuthRequest request){
        return findByUsername(request.email())
            .filter(userDetails -> passwordEncoder.matches(request.password(), userDetails.getPassword()))
            .filter(UserDetails::isEnabled)
            .filter(UserDetails::isAccountNonExpired)
            .filter(UserDetails::isAccountNonLocked)
            .filter(UserDetails::isCredentialsNonExpired);
    }

    @Override
    public Mono<UserDetails> findByUsername(String email) {
        return userRepository.findByEmail(email)
                .cast(UserDetails.class)
                .switchIfEmpty(Mono.error(() -> new RuntimeException("User doesn't exist")) );
    }

    // async predicate
    private Mono<Boolean> userNotExistsInDBByEmail(User user){
        return userRepository.findByEmail(user.getEmail())
                .flatMap(userFromDB -> Mono.just(false))
                .switchIfEmpty(Mono.just(true));
    }

    private UserValidator.ValidationResult isUserValid(User user){
        UserValidator.ValidationResult result = UserValidator
                .isUsernameValid()
                .and(UserValidator.isPasswordValid())
                .and(UserValidator.isFirstNameValid())
                .and(UserValidator.isLastNameValid())
                .and(UserValidator.isRoleProvided())
                .apply(user);
        return result;
    }

    private Role getDefaultRole() {
        var bookReadAuthority = Authority.builder()
                .authorityName("book:read")
                .build();
        return Role.builder()
                .roleName("booklister")
                .authorities(List.of(bookReadAuthority))
                .build();
    }
}
