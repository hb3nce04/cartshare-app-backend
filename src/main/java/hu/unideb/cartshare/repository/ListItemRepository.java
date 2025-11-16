package hu.unideb.cartshare.repository;

import hu.unideb.cartshare.model.entity.ListItem;
import hu.unideb.cartshare.repository.common.BaseRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository interface for {@link hu.unideb.cartshare.model.entity.ListItem}.
 */
@Repository
public interface ListItemRepository extends BaseRepository<ListItem> {
}
