package hu.unideb.cartshare.repository;

import java.util.UUID;

import org.springframework.stereotype.Repository;

import hu.unideb.cartshare.model.entity.List;
import hu.unideb.cartshare.repository.common.BaseRepository;

@Repository
public interface ListRepository extends BaseRepository<List> {
    //java.util.List<List> findAllByUser_Id(UUID userId);
}
