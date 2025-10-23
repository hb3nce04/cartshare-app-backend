package hu.unideb.cartshare.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Repository;

import hu.unideb.cartshare.model.entity.UserListMembership;
import hu.unideb.cartshare.model.enums.UserListRole;
import hu.unideb.cartshare.repository.common.BaseRepository;

@Repository
public interface UserListMembershipRepository extends BaseRepository<UserListMembership> {
    List<UserListMembership> findAllByUser_IdAndRoleIs(
            UUID userId,
            UserListRole role);

    boolean existsByListAndUser_IdAndRoleIs(
            hu.unideb.cartshare.model.entity.List list,
            UUID user_id,
            UserListRole role);

    void deleteByListAndUser_Id(
            hu.unideb.cartshare.model.entity.List list,
            UUID userId);
}
