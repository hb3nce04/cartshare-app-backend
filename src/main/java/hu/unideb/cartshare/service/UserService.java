package hu.unideb.cartshare.service;

import java.util.UUID;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import hu.unideb.cartshare.exception.BusinessLogicException;
import hu.unideb.cartshare.mapper.UserMapper;
import hu.unideb.cartshare.model.dto.request.UserRequestDto;
import hu.unideb.cartshare.model.dto.response.UserResponseDto;
import hu.unideb.cartshare.model.entity.User;
import hu.unideb.cartshare.repository.UserRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository repository;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper mapper;

    public UserResponseDto create(UserRequestDto dto) {
        if (repository.existsByUsername(dto.getUsername())) {
            throw new BusinessLogicException("Ez a felhasználónév már foglalt!");
        }
        if (repository.existsByEmail(dto.getEmail())) {
            throw new BusinessLogicException("Ez az e-mail cím már foglalt!");
        }

        String encodedPassword = passwordEncoder.encode(dto.getPassword());

        User user = new User();
        user.setUsername(dto.getUsername());
        user.setEmail(dto.getEmail());
        user.setPassword(encodedPassword);

        repository.save(user);

        return mapper.toDto(user);
    }

    public User findById(UUID id) {
        return repository.findById(id).orElseThrow(() -> new IllegalArgumentException("Felhasználó nem található!"));
    }
}
