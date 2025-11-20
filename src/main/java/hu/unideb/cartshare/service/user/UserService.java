package hu.unideb.cartshare.service.user;

import java.util.UUID;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import hu.unideb.cartshare.exception.BusinessLogicException;
import hu.unideb.cartshare.mapper.UserMapper;
import hu.unideb.cartshare.model.dto.request.UserRequestDto;
import hu.unideb.cartshare.model.dto.response.UserResponseDto;
import hu.unideb.cartshare.model.entity.User;
import hu.unideb.cartshare.model.enums.AuthProvider;
import hu.unideb.cartshare.repository.UserRepository;
import lombok.RequiredArgsConstructor;

/**
 * Handles user management business logic.
 */
@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository repository;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper mapper;

    /**
     * Creates a new local user in the db.
     * @param dto {@link UserRequestDto} request DTO
     * @return {@link UserResponseDto} response DTO
     */
    public UserResponseDto createLocalUser(UserRequestDto dto) {
        if (repository.existsByUsername(dto.getUsername())) {
            throw new BusinessLogicException("This username is already taken.");
        }
        if (repository.existsByEmail(dto.getEmail())) {
            throw new BusinessLogicException("This e-mail address is already taken.");
        }

        String encodedPassword = passwordEncoder.encode(dto.getPassword());

        User user = new User();
        user.setUsername(dto.getUsername());
        user.setEmail(dto.getEmail());
        user.setPassword(encodedPassword);
        user.setProvider(AuthProvider.LOCAL);

        repository.save(user);

        return mapper.toDto(user);
    }

    /**
     * Finds and creates (when it doesn't exist) a Google user in the db.
     * @param email e-mail address
     * @param username username
     * @param googleId google profile ID
     * @return {@link hu.unideb.cartshare.model.entity.User} user entity
     */
    public User findOrCreateGoogleUser(String email, String username, String googleId) {
        return repository.findByEmail(email).orElseGet(
                () -> {
                    User newUser = new User();
                    newUser.setUsername(username);
                    newUser.setEmail(email);
                    newUser.setProvider(AuthProvider.GOOGLE);
                    newUser.setProviderId(googleId);
                    return repository.save(newUser);
                }
        );
    }

    /**
     * Finds the user in the DB by UUID.
     * @param id {@link java.util.UUID} id
     * @return {@link hu.unideb.cartshare.model.entity.User} user entity
     */
    public User findById(UUID id) {
        return repository.findById(id).orElseThrow(() -> new IllegalArgumentException("User not exists."));
    }
}
