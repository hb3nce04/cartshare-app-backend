package hu.unideb.cartshare.service;

import java.util.Optional;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import hu.unideb.cartshare.model.entity.User;
import hu.unideb.cartshare.model.entity.UserDetailsImpl;
import hu.unideb.cartshare.repository.UserRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {
    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> foundUser = userRepository.findByUsername(username);
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
}
