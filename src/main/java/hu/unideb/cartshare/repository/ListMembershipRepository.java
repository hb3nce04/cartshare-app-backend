package hu.unideb.cartshare.repository;

import hu.unideb.cartshare.model.entity.ListMembership;
import hu.unideb.cartshare.model.enums.MembershipRole;
import hu.unideb.cartshare.repository.common.BaseRepository;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

/**
 * Repository interface for {@link hu.unideb.cartshare.model.entity.ListMembership}.
 */
@Repository
public interface ListMembershipRepository
        extends BaseRepository<ListMembership> {
    List<ListMembership> findAllByUser_IdAndRoleIs(
            UUID userId,
            MembershipRole role);

    boolean existsByListAndUserId(
            hu.unideb.cartshare.model.entity.List list,
            UUID userId);

    boolean existsByListAndUserIdAndRoleIs(
            hu.unideb.cartshare.model.entity.List list,
            UUID userId,
            MembershipRole role);

    @Modifying
    @Transactional
    void deleteByListAndUserId(
            hu.unideb.cartshare.model.entity.List list,
            UUID userId);
}
