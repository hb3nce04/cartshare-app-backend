package hu.unideb.cartshare.repository;

import org.springframework.stereotype.Repository;

import hu.unideb.cartshare.model.entity.ListItem;
import hu.unideb.cartshare.repository.common.BaseRepository;

@Repository
public interface ListItemRepository extends BaseRepository<ListItem> {
}
