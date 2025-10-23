package hu.unideb.cartshare.repository;

import org.springframework.stereotype.Repository;

import hu.unideb.cartshare.model.entity.User;
import hu.unideb.cartshare.repository.common.BaseRepository;

@Repository
public interface UserRepository extends BaseRepository<User> {
    boolean existsByUsername(String username);
    boolean existsByEmail(String email);
}
