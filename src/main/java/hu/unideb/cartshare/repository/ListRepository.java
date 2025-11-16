package hu.unideb.cartshare.repository;

import hu.unideb.cartshare.model.entity.List;
import hu.unideb.cartshare.model.entity.ListItem;
import hu.unideb.cartshare.repository.common.BaseRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repository interface for {@link hu.unideb.cartshare.model.entity.List}.
 */
@Repository
public interface ListRepository extends BaseRepository<List> {
    Optional<List> findByItemsContaining(ListItem items);
}
