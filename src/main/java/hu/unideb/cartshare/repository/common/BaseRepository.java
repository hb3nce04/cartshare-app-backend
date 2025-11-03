package hu.unideb.cartshare.repository.common;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

import hu.unideb.cartshare.model.entity.common.BaseEntity;

/**
 * Makes reusable {@link org.springframework.data.jpa.repository.JpaRepository} extending the {@link hu.unideb.cartshare.model.entity.common.BaseEntity}.
 */
@NoRepositoryBean
public interface BaseRepository<T extends BaseEntity> extends JpaRepository<T, UUID> {
}
