package hu.unideb.cartshare.service;

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

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {
    private final UserRepository userRepository;

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
