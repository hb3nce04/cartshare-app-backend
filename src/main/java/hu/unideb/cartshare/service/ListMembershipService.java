package hu.unideb.cartshare.service;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;

import hu.unideb.cartshare.exception.BusinessLogicException;
import hu.unideb.cartshare.model.entity.ListMembership;
import hu.unideb.cartshare.model.enums.MembershipRole;
import hu.unideb.cartshare.repository.ListMembershipRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ListMembershipService {
    private final ListMembershipRepository repository;
    private final UserService userService;

    public List<ListMembership> findAllOwnership() {
        return repository.findAllByUser_IdAndRoleIs(getCurrentUserId(), MembershipRole.OWNER);
    }

    public List<ListMembership> findAllMembership() {
        return repository.findAllByUser_IdAndRoleIs(getCurrentUserId(), MembershipRole.MEMBER);
    }

    public void join(
            hu.unideb.cartshare.model.entity.List list,
            MembershipRole role) {
        if (!isOwner(list) && !isMember(list)) {
            ListMembership membership = new ListMembership();
            // TODO: joinedAt ?
            membership.setList(list);
            membership.setUser(userService.findById(getCurrentUserId()));
            membership.setRole(role);
            repository.save(membership);
        }
    }

    public void leave(hu.unideb.cartshare.model.entity.List list) {
        if (isMember(list)) {
            repository.deleteByListAndUserId(list, getCurrentUserId());
        }
    }

    public boolean isMember(hu.unideb.cartshare.model.entity.List list) {
        return checkMembership(list, MembershipRole.MEMBER);
    }

    public boolean isOwner(hu.unideb.cartshare.model.entity.List list) {
        return checkMembership(list, MembershipRole.OWNER);
    }

    public boolean hasAnyMembershipInList(hu.unideb.cartshare.model.entity.List list) {
        return checkMembership(list, null);
    }

    private boolean checkMembership(
            hu.unideb.cartshare.model.entity.List list,
            MembershipRole requiredRole) {
        UUID userId = getCurrentUserId();
        boolean exists;

        if (requiredRole != null) {
            exists = repository.existsByListAndUserIdAndRoleIs(list, userId, requiredRole);
        } else {
            exists = repository.existsByListAndUserId(list, userId);
        }

        if (!exists) {
            throw new BusinessLogicException(
                    requiredRole == null
                            ? "Nem vagy bennne a listában!"
                            : "Nem vagy a tulajdonosa a listának!"
            );
        }

        return true;
    }

    private UUID getCurrentUserId() {
        return UUID.fromString("6556437c-2a30-4e10-bfee-7239c5fc9e12");
    }
}
