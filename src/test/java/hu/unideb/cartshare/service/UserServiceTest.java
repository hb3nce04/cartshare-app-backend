package hu.unideb.cartshare.service;

import hu.unideb.cartshare.exception.BusinessLogicException;
import hu.unideb.cartshare.mapper.UserMapper;
import hu.unideb.cartshare.model.dto.request.UserRequestDto;
import hu.unideb.cartshare.model.dto.response.UserResponseDto;
import hu.unideb.cartshare.model.entity.User;
import hu.unideb.cartshare.model.enums.AuthProvider;
import hu.unideb.cartshare.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository repository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private UserMapper mapper;

    @InjectMocks
    private UserService userService;

    private UserRequestDto dto;
    private User user;
    private UserResponseDto response;
    private UUID id;

    @BeforeEach
    void setUp() {
        id = UUID.randomUUID();

        dto = new UserRequestDto();
        dto.setUsername("tester");
        dto.setEmail("test@example.com");
        dto.setPassword("secret");

        user = new User();
        user.setId(id);
        user.setUsername("tester");
        user.setEmail("test@example.com");
        user.setPassword("encoded-secret");

        response = new UserResponseDto();
        response.setUsername("tester");
        response.setEmail("test@example.com");
    }

    @Test
    void shouldCreateLocalUserSuccessfully() {
        // given
        when(repository.existsByUsername(dto.getUsername())).thenReturn(false);
        when(repository.existsByEmail(dto.getEmail())).thenReturn(false);
        when(passwordEncoder.encode(dto.getPassword())).thenReturn("encoded-secret");
        when(mapper.toDto(any(User.class))).thenReturn(response);

        // when
        UserResponseDto result = userService.createLocalUser(dto);

        // then
        assertNotNull(result);
        assertEquals(dto.getUsername(), result.getUsername());
        verify(repository).save(any(User.class));
        verify(passwordEncoder).encode("secret");
    }

    @Test
    void shouldThrowExceptionWhenUsernameExists() {

        when(repository.existsByUsername(dto.getUsername())).thenReturn(true);

        assertThrows(BusinessLogicException.class, () -> userService.createLocalUser(dto));
        verify(repository, never()).save(any());
    }

    @Test
    void shouldThrowExceptionWhenEmailExists() {

        when(repository.existsByUsername(dto.getUsername())).thenReturn(false);
        when(repository.existsByEmail(dto.getEmail())).thenReturn(true);

        assertThrows(BusinessLogicException.class, () -> userService.createLocalUser(dto));
        verify(repository, never()).save(any());
    }


    @Test
    void shouldReturnExistingUserWhenEmailFound() {

        when(repository.findByEmail("test@example.com")).thenReturn(Optional.of(user));

        User result = userService.findOrCreateGoogleUser("test@example.com", "tester", "google123");

        assertEquals(user, result);
        verify(repository, never()).save(any());
    }

    @Test
    void shouldCreateNewUserWhenEmailNotFound() {

        when(repository.findByEmail("new@example.com")).thenReturn(Optional.empty());
        when(repository.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));

        User result = userService.findOrCreateGoogleUser("new@example.com", "newuser", "google456");

        assertNotNull(result);
        assertEquals("new@example.com", result.getEmail());
        assertEquals(AuthProvider.GOOGLE, result.getProvider());
        verify(repository).save(any(User.class));
    }

    @Test
    void shouldReturnUserWhenIdExists() {
        // given
        when(repository.findById(id)).thenReturn(Optional.of(user));

        // when
        User result = userService.findById(id);

        // then
        assertNotNull(result);
        assertEquals(id, result.getId());
        verify(repository).findById(id);
    }

    @Test
    void shouldThrowExceptionWhenUserNotFoundById() {

        when(repository.findById(id)).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () -> userService.findById(id));
    }
}
