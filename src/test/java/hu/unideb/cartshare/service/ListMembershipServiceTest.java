package hu.unideb.cartshare.service;

import hu.unideb.cartshare.exception.BusinessLogicException;
import hu.unideb.cartshare.model.UserDetailsImpl;
import hu.unideb.cartshare.model.entity.ListMembership;
import hu.unideb.cartshare.model.entity.User;
import hu.unideb.cartshare.model.enums.MembershipRole;
import hu.unideb.cartshare.repository.ListMembershipRepository;
import hu.unideb.cartshare.service.user.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ListMembershipServiceTest {

    @Mock
    private ListMembershipRepository repository;

    @Mock
    private UserService userService;

    @Mock
    private Authentication authentication;

    @Mock
    private SecurityContext securityContext;

    @InjectMocks
    private ListMembershipService service;

    private UUID userId;
    private hu.unideb.cartshare.model.entity.List list;
    private UserDetailsImpl principal;
    private User user;

    @BeforeEach
    void setUp() {
        userId = UUID.randomUUID();
        list = new hu.unideb.cartshare.model.entity.List();
        list.setId(UUID.randomUUID());

        user = new User();
        user.setId(userId);

        principal = UserDetailsImpl.builder()
                .id(userId)
                .username("tester")
                .password("encoded")
                .build();

        SecurityContextHolder.clearContext();
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);
        when(authentication.isAuthenticated()).thenReturn(true);
        when(authentication.getPrincipal()).thenReturn(principal);
    }

    @Test
    void findAllOwnership_shouldReturnOwnerMemberships() {
        var memberships = List.of(new ListMembership());
        when(repository.findAllByUser_IdAndRoleIs(userId, MembershipRole.OWNER)).thenReturn(memberships);

        var result = service.findAllOwnership();

        assertEquals(memberships, result);
        verify(repository).findAllByUser_IdAndRoleIs(userId, MembershipRole.OWNER);
    }

    @Test
    void findAllMembership_shouldReturnMemberMemberships() {
        var memberships = List.of(new ListMembership());
        when(repository.findAllByUser_IdAndRoleIs(userId, MembershipRole.MEMBER)).thenReturn(memberships);

        var result = service.findAllMembership();

        assertEquals(memberships, result);
        verify(repository).findAllByUser_IdAndRoleIs(userId, MembershipRole.MEMBER);
    }

    @Test
    void join_shouldCreateMembership_whenNotMemberYet() {
        when(repository.existsByListAndUserId(list, userId)).thenReturn(false);
        when(userService.findById(userId)).thenReturn(user);

        service.join(list, MembershipRole.MEMBER);

        verify(repository).save(any(ListMembership.class));
    }

    @Test
    void join_shouldThrowException_whenAlreadyMember() {
        when(repository.existsByListAndUserId(list, userId)).thenReturn(true);

        BusinessLogicException ex = assertThrows(BusinessLogicException.class,
                () -> service.join(list, MembershipRole.MEMBER));

        assertEquals("You already in the list.", ex.getMessage());
        verify(repository, never()).save(any());
    }

    @Test
    void leave_shouldDeleteMembership_whenUserIsMember() {
        when(repository.existsByListAndUserIdAndRoleIs(list, userId, MembershipRole.MEMBER))
                .thenReturn(true);

        service.leave(list);

        verify(repository).deleteByListAndUserId(list, userId);
    }

    @Test
    void leave_shouldThrowException_whenNotMember() {
        when(repository.existsByListAndUserIdAndRoleIs(list, userId, MembershipRole.MEMBER))
                .thenReturn(false);

        assertThrows(BusinessLogicException.class, () -> service.leave(list));
        verify(repository, never()).deleteByListAndUserId(any(), any());
    }

    @Test
    void isMember_shouldReturnTrue_whenUserIsMember() {
        when(repository.existsByListAndUserIdAndRoleIs(list, userId, MembershipRole.MEMBER))
                .thenReturn(true);

        assertTrue(service.isMember(list));
    }

    @Test
    void isMember_shouldThrowException_whenNotMember() {
        when(repository.existsByListAndUserIdAndRoleIs(list, userId, MembershipRole.MEMBER))
                .thenReturn(false);

        BusinessLogicException ex = assertThrows(BusinessLogicException.class, () -> service.isMember(list));
        assertEquals("You are not the member of this list.", ex.getMessage());
    }

    @Test
    void isOwner_shouldReturnTrue_whenUserIsOwner() {
        when(repository.existsByListAndUserIdAndRoleIs(list, userId, MembershipRole.OWNER))
                .thenReturn(true);

        assertTrue(service.isOwner(list));
    }

    @Test
    void isOwner_shouldThrowException_whenNotOwner() {
        when(repository.existsByListAndUserIdAndRoleIs(list, userId, MembershipRole.OWNER))
                .thenReturn(false);

        BusinessLogicException ex = assertThrows(BusinessLogicException.class, () -> service.isOwner(list));
        assertEquals("You are not the owner of this list.", ex.getMessage());
    }

    @Test
    void hasAnyMembershipInList_shouldReturnTrue_whenUserHasAnyRole() {
        when(repository.existsByListAndUserId(list, userId)).thenReturn(true);

        assertTrue(service.hasAnyMembershipInList(list));
    }

    @Test
    void hasAnyMembershipInList_shouldThrowException_whenUserHasNoMembership() {
        when(repository.existsByListAndUserId(list, userId)).thenReturn(false);

        BusinessLogicException ex = assertThrows(BusinessLogicException.class, () -> service.hasAnyMembershipInList(list));
        assertEquals("You are not in the list.", ex.getMessage());
    }

    /*@Test
    void getCurrentUserId_shouldThrowException_whenNotAuthenticated() {
        when(authentication.isAuthenticated()).thenReturn(false);

        assertThrows(BusinessLogicException.class, () -> {
            var method = ListMembershipService.class.getDeclaredMethod("getCurrentUserId");
            method.setAccessible(true);
            method.invoke(service);
        });
    }*/
}
