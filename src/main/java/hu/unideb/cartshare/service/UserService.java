package hu.unideb.cartshare.service;

import java.util.UUID;

import org.springframework.stereotype.Service;

import hu.unideb.cartshare.model.entity.User;
import hu.unideb.cartshare.repository.UserRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository repository;

    public User findById(UUID id) {
        return repository.findById(id).orElseThrow(() -> new IllegalArgumentException("Felhasználó nem található!"));
    }
}
