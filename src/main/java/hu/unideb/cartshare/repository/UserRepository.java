package hu.unideb.cartshare.repository;

import hu.unideb.cartshare.model.entity.User;
import hu.unideb.cartshare.model.enums.AuthProvider;
import hu.unideb.cartshare.repository.common.BaseRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repository interface for {@link hu.unideb.cartshare.model.entity.User}.
 */
@Repository
public interface UserRepository extends BaseRepository<User> {
    boolean existsByUsername(String username);

    boolean existsByEmail(String email);

    Optional<User> findByUsernameAndProvider(
            String username,
            AuthProvider provider);

    Optional<User> findByEmail(String email);
}
