package com.example.demo.service;

import com.example.demo.dto.UserRequest;
import com.example.demo.model.Role;
import com.example.demo.model.User;
import com.example.demo.repository.RoleRepository;
import com.example.demo.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public void register(UserRequest request) {

        if (request.getLogin() == null || request.getLogin().isBlank()) {
            throw new IllegalArgumentException("Login cannot be empty");
        }

        if (request.getLogin().length() < 3 || request.getLogin().length() > 20) {
            throw new IllegalArgumentException("Login must be between 3 and 20 characters");
        }

        if (userRepository.findByLogin(request.getLogin()).isPresent()) {
            throw new IllegalArgumentException("User with this login already exists");
        }

        Role userRole = roleRepository.findByRole("ROLE_USER")
                .orElseThrow(() -> new IllegalStateException("ROLE_USER not found"));

        User user = new User();
        user.setId(UUID.randomUUID().toString());
        user.setLogin(request.getLogin());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRoles(Set.of(userRole));    // ROLE_USER by default

        userRepository.save(user);
    }

    public Optional<User> findByLogin(String login) {
        return userRepository.findByLogin(login);
    }
}
