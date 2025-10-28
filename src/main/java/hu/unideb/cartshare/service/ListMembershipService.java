package hu.unideb.cartshare.service;

import java.util.List;
import java.util.UUID;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import hu.unideb.cartshare.exception.BusinessLogicException;
import hu.unideb.cartshare.model.entity.ListMembership;
import hu.unideb.cartshare.model.entity.UserDetailsImpl;
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
        if (hasNoMembershipInList(list)) {
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
                            : "Nem a " + (requiredRole == MembershipRole.OWNER ? "tulajdonosa" : "tagja") + " vagy a listának!"
            );
        }

        return true;
    }

    private boolean hasNoMembershipInList(hu.unideb.cartshare.model.entity.List list) {
        UUID userId = getCurrentUserId();
        boolean exists = repository.existsByListAndUserId(list, userId);

        if (exists) {
            throw new BusinessLogicException("Már benne vagy a listában!");
        }

        return true;
    }

    private UUID getCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            throw new BusinessLogicException("Nem sikerült a felhasználó azonosítása!");
        }

        UserDetailsImpl principal = (UserDetailsImpl) authentication.getPrincipal();

        return principal.getId();
    }
}
