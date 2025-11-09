package hu.unideb.cartshare.service.user;

import hu.unideb.cartshare.model.UserDetailsImpl;
import hu.unideb.cartshare.model.entity.User;
import hu.unideb.cartshare.model.enums.AuthProvider;
import hu.unideb.cartshare.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserDetailsServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserDetailsServiceImpl userDetailsService;

    private User user;
    private UUID id;

    @BeforeEach
    void setUp() {
        id = UUID.randomUUID();
        user = new User();
        user.setId(id);
        user.setUsername("tester");
        user.setPassword("encoded-secret");
        user.setProvider(AuthProvider.LOCAL);
    }

    @Test
    void loadUserByUsername_shouldReturnUserDetails_whenUserExists() {
        when(userRepository.findByUsernameAndProvider("tester", AuthProvider.LOCAL))
                .thenReturn(Optional.of(user));

        var result = userDetailsService.loadUserByUsername("tester");

        assertNotNull(result);
        assertEquals(user.getUsername(), result.getUsername());
        assertEquals(user.getPassword(), result.getPassword());
        verify(userRepository).findByUsernameAndProvider("tester", AuthProvider.LOCAL);
    }

    @Test
    void loadUserByUsername_shouldThrowException_whenUserNotFound() {
        when(userRepository.findByUsernameAndProvider("tester", AuthProvider.LOCAL))
                .thenReturn(Optional.empty());

        UsernameNotFoundException ex = assertThrows(UsernameNotFoundException.class,
                () -> userDetailsService.loadUserByUsername("tester"));

        assertEquals("Hibás felhasználónév vagy jelszó.", ex.getMessage());
    }

    @Test
    void loadUserById_shouldReturnUserDetails_whenUserExists() {
        when(userRepository.findById(id)).thenReturn(Optional.of(user));

        var result = userDetailsService.loadUserById(id);

        assertNotNull(result);
        assertEquals(user.getId(), ((UserDetailsImpl) result).getId());
        assertEquals(user.getUsername(), result.getUsername());
        verify(userRepository).findById(id);
    }

    @Test
    void loadUserById_shouldThrowException_whenUserNotFound() {
        when(userRepository.findById(id)).thenReturn(Optional.empty());

        UsernameNotFoundException ex = assertThrows(UsernameNotFoundException.class,
                () -> userDetailsService.loadUserById(id));

        assertEquals("Hibás azonosító.", ex.getMessage());
    }
}
