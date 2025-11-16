package hu.unideb.cartshare.repository.common;

import hu.unideb.cartshare.model.entity.common.BaseEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

import java.util.UUID;

/**
 * Makes reusable {@link org.springframework.data.jpa.repository.JpaRepository} extending the {@link hu.unideb.cartshare.model.entity.common.BaseEntity}.
 */
@NoRepositoryBean
public interface BaseRepository<T extends BaseEntity>
        extends JpaRepository<T, UUID> {
}
