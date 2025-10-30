package hu.unideb.cartshare.repository;

import java.util.Optional;

import org.springframework.stereotype.Repository;

import hu.unideb.cartshare.model.entity.User;
import hu.unideb.cartshare.model.enums.AuthProvider;
import hu.unideb.cartshare.repository.common.BaseRepository;

@Repository
public interface UserRepository extends BaseRepository<User> {
    boolean existsByUsername(String username);
    boolean existsByEmail(String email);

    Optional<User> findByUsernameAndProvider(
            String username,
            AuthProvider provider);

    Optional<User> findByEmail(String email);
}
