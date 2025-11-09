package hu.unideb.cartshare.service;

import java.util.List;
import java.util.UUID;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import hu.unideb.cartshare.exception.BusinessLogicException;
import hu.unideb.cartshare.model.UserDetailsImpl;
import hu.unideb.cartshare.model.entity.ListMembership;
import hu.unideb.cartshare.model.enums.MembershipRole;
import hu.unideb.cartshare.repository.ListMembershipRepository;
import hu.unideb.cartshare.service.user.UserService;
import lombok.RequiredArgsConstructor;

/**
 * Handles list membership management business logic.
 */
@Service
@RequiredArgsConstructor
public class ListMembershipService {
    private final ListMembershipRepository repository;
    private final UserService userService;

    /**
     * Find all {@link hu.unideb.cartshare.model.entity.ListMembership} entry where the currently authenticated user is an OWNER.
     * @return {@link List} of {@link hu.unideb.cartshare.model.entity.ListMembership}
     */
    public List<ListMembership> findAllOwnership() {
        return repository.findAllByUser_IdAndRoleIs(getCurrentUserId(), MembershipRole.OWNER);
    }

    /**
     * Find all {@link hu.unideb.cartshare.model.entity.ListMembership} entry where the currently authenticated user is a MEMBER.
     * @return {@link List} of {@link hu.unideb.cartshare.model.entity.ListMembership}
     */
    public List<ListMembership> findAllMembership() {
        return repository.findAllByUser_IdAndRoleIs(getCurrentUserId(), MembershipRole.MEMBER);
    }

    /**
     * Joining to a specific list by id when the user already is not a member.
     * @param list {@link hu.unideb.cartshare.model.entity.List} list entity
     * @param role {@link MembershipRole} role enum
     */
    public void join(
            hu.unideb.cartshare.model.entity.List list,
            MembershipRole role) {
        if (hasNoMembershipInList(list)) {
            ListMembership membership = new ListMembership();
            membership.setList(list);
            membership.setUser(userService.findById(getCurrentUserId()));
            membership.setRole(role);
            repository.save(membership);
        }
    }

    /**
     * Leaving from a specific list when the user already is a member.
     * @param list {@link hu.unideb.cartshare.model.entity.List} list entity
     */
    public void leave(hu.unideb.cartshare.model.entity.List list) {
        if (isMember(list)) {
            repository.deleteByListAndUserId(list, getCurrentUserId());
        }
    }

    /**
     * Checks whether the currently authenticated user is a MEMBER in a specific list.
     * @param list {@link hu.unideb.cartshare.model.entity.List} list entity
     * @return boolean primitive - true / false
     */
    public boolean isMember(hu.unideb.cartshare.model.entity.List list) {
        return checkMembership(list, MembershipRole.MEMBER);
    }

    /**
     * Checks whether the currently authenticated user is an OWNER in a specific list.
     * @param list {@link hu.unideb.cartshare.model.entity.List} list entity
     * @return boolean primitive - true / false
     */
    public boolean isOwner(hu.unideb.cartshare.model.entity.List list) {
        return checkMembership(list, MembershipRole.OWNER);
    }

    /**
     * Checks whether the currently authenticated user is an OWNER/MEMBER in a specific list.
     * @param list {@link hu.unideb.cartshare.model.entity.List} list entity
     * @return boolean primitive - true / false
     */
    public boolean hasAnyMembershipInList(hu.unideb.cartshare.model.entity.List list) {
        return checkMembership(list, null);
    }

    /**
     * Reuses the role permission checks in a list with a specific user.
     * @param list {@link hu.unideb.cartshare.model.entity.List} list entity
     * @param requiredRole {@link hu.unideb.cartshare.model.enums.MembershipRole} enum
     * @return boolean primitive - true / false
     */
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
                            ? "Nem vagy bennne a listában."
                            : "Nem a " + (requiredRole == MembershipRole.OWNER ? "tulajdonosa" : "tagja") + " vagy a listának."
            );
        }

        return true;
    }

    /**
     * Checks whether the currently authenticated user is not in a specific list.
     * @param list {@link hu.unideb.cartshare.model.entity.List} list entity
     * @return boolean primitive - true / false
     */
    private boolean hasNoMembershipInList(hu.unideb.cartshare.model.entity.List list) {
        UUID userId = getCurrentUserId();
        boolean exists = repository.existsByListAndUserId(list, userId);

        if (exists) {
            throw new BusinessLogicException("Már benne vagy a listában.");
        }

        return true;
    }

    /**
     * Gets the currently authenticated user ID from the Spring Security. {@link org.springframework.security.core.context.SecurityContextHolder}
     * @return {@link UUID} the currently authenticated user ID
     */
    private UUID getCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            throw new BusinessLogicException("Nem sikerült a felhasználó azonosítása.");
        }

        UserDetailsImpl principal = (UserDetailsImpl) authentication.getPrincipal();

        return principal.getId();
    }
}
