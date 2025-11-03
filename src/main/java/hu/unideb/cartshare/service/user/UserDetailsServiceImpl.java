package hu.unideb.cartshare.service.user;

import java.util.Optional;
import java.util.UUID;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import hu.unideb.cartshare.model.UserDetailsImpl;
import hu.unideb.cartshare.model.entity.User;
import hu.unideb.cartshare.model.enums.AuthProvider;
import hu.unideb.cartshare.repository.UserRepository;
import lombok.RequiredArgsConstructor;

/**
 * Custom Spring Security {@link org.springframework.security.core.userdetails.UserDetailsService} interface implementation.
 */
@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {
    private final UserRepository userRepository;

    /**
     * Querying users by username.
     * @param username the user's username
     * @return {@link UserDetails} interface
     * @throws UsernameNotFoundException message: "Hibás felhasználónév vagy jelszó"
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> foundUser = userRepository.findByUsernameAndProvider(username, AuthProvider.LOCAL);
        if (foundUser.isEmpty()) {
            throw new UsernameNotFoundException("Hibás felhasználónév vagy jelszó");
        }
        return UserDetailsImpl
                .builder()
                .id(foundUser.get().getId())
                .username(foundUser.get().getUsername())
                .password(foundUser.get().getPassword())
                .build();
    }

    /**
     * Querying users by id.
     * @param id the user's {@link java.util.UUID}
     * @return {@link UserDetails} interface
     * @throws UsernameNotFoundException message: "Hibás felhasználónév vagy jelszó"
     */
    public UserDetails loadUserById(UUID id) throws UsernameNotFoundException {
        Optional<User> foundUser = userRepository.findById(id);
        if (foundUser.isEmpty()) {
            throw new UsernameNotFoundException("Hibás azonosító");
        }
        return UserDetailsImpl
                .builder()
                .id(foundUser.get().getId())
                .username(foundUser.get().getUsername())
                .password(foundUser.get().getPassword())
                .build();
    }
}
