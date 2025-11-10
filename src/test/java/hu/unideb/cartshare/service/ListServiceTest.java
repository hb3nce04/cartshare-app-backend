package hu.unideb.cartshare.service;

import hu.unideb.cartshare.exception.BusinessLogicException;
import hu.unideb.cartshare.mapper.ListMapper;
import hu.unideb.cartshare.model.dto.request.ListRequestDto;
import hu.unideb.cartshare.model.dto.response.ListResponseDto;
import hu.unideb.cartshare.model.entity.ListItem;
import hu.unideb.cartshare.model.entity.ListMembership;
import hu.unideb.cartshare.model.enums.MembershipRole;
import hu.unideb.cartshare.repository.ListRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ListServiceTest {

    @Mock
    private ListRepository repository;

    @Mock
    private ListMapper mapper;

    @Mock
    private ListMembershipService listMembershipService;

    @InjectMocks
    private ListService listService;

    private hu.unideb.cartshare.model.entity.List listEntity;
    private ListResponseDto responseDto;

    @BeforeEach
    void setUp() {
        listEntity = new hu.unideb.cartshare.model.entity.List();
        listEntity.setId(UUID.randomUUID());
        listEntity.setName("Teszt lista");

        responseDto = new ListResponseDto();
        responseDto.setId(listEntity.getId());
        responseDto.setName(listEntity.getName());
    }

    @Test
    void shouldReturnOwnedLists_whenUserIsOwner() {
        ListMembership membership = new ListMembership();
        membership.setList(listEntity);

        when(listMembershipService.findAllOwnership()).thenReturn(List.of(membership));
        when(mapper.toDtoList(anyList())).thenReturn(List.of(responseDto));

        var result = listService.findAllOwnedLists();

        assertEquals(1, result.size());
        verify(listMembershipService).findAllOwnership();
        verify(mapper).toDtoList(anyList());
    }

    @Test
    void shouldReturnJoinedLists_whenUserIsMember() {
        ListMembership membership = new ListMembership();
        membership.setList(listEntity);

        when(listMembershipService.findAllMembership()).thenReturn(List.of(membership));
        when(mapper.toDtoList(anyList())).thenReturn(List.of(responseDto));

        var result = listService.findAllAllJoinedLists();

        assertEquals(1, result.size());
        verify(listMembershipService).findAllMembership();
        verify(mapper).toDtoList(anyList());
    }

    @Test
    void shouldCreateListAndJoinAsOwner() {
        ListRequestDto dto = new ListRequestDto();
        dto.setName("Új lista");

        when(mapper.toDto(any())).thenReturn(responseDto);

        var result = listService.create(dto);

        assertNotNull(result);
        verify(repository).save(any());
        verify(listMembershipService).join(any(), eq(MembershipRole.OWNER));
        verify(mapper).toDto(any());
    }

    @Test
    void shouldJoinListById() {
        UUID id = UUID.randomUUID();
        when(repository.findById(id)).thenReturn(Optional.of(listEntity));
        when(mapper.toDto(any())).thenReturn(responseDto);

        var result = listService.joinById(id);

        assertEquals(responseDto, result);
        verify(listMembershipService).join(listEntity, MembershipRole.MEMBER);
    }

    @Test
    void shouldUpdateList_whenUserIsOwner() {
        UUID id = UUID.randomUUID();
        ListRequestDto dto = new ListRequestDto();
        dto.setName("Frissített név");

        when(repository.findById(id)).thenReturn(Optional.of(listEntity));
        when(listMembershipService.isOwner(listEntity)).thenReturn(true);

        listService.update(id, dto);

        verify(repository).save(listEntity);
        assertEquals("Frissített név", listEntity.getName());
    }

    @Test
    void shouldNotUpdateList_whenUserIsNotOwner() {
        UUID id = UUID.randomUUID();
        ListRequestDto dto = new ListRequestDto();
        dto.setName("Nem mentődik");

        when(repository.findById(id)).thenReturn(Optional.of(listEntity));
        when(listMembershipService.isOwner(listEntity)).thenReturn(false);

        listService.update(id, dto);

        verify(repository, never()).save(any());
    }

    @Test
    void shouldLeaveListById() {
        UUID id = UUID.randomUUID();
        when(repository.findById(id)).thenReturn(Optional.of(listEntity));

        listService.leaveById(id);

        verify(listMembershipService).leave(listEntity);
    }

    @Test
    void shouldDeleteList_whenUserIsOwner() {
        UUID id = UUID.randomUUID();
        when(repository.findById(id)).thenReturn(Optional.of(listEntity));
        when(listMembershipService.isOwner(listEntity)).thenReturn(true);

        listService.delete(id);

        verify(repository).delete(listEntity);
    }

    @Test
    void shouldNotDeleteList_whenUserIsNotOwner() {
        UUID id = UUID.randomUUID();
        when(repository.findById(id)).thenReturn(Optional.of(listEntity));
        when(listMembershipService.isOwner(listEntity)).thenReturn(false);

        listService.delete(id);

        verify(repository, never()).delete(any());
    }

    @Test
    void shouldReturnList_whenExists() {
        UUID id = UUID.randomUUID();
        when(repository.findById(id)).thenReturn(Optional.of(listEntity));

        var result = listService.findById(id);

        assertEquals(listEntity, result);
    }

    @Test
    void shouldThrow_whenNotFound() {
        UUID id = UUID.randomUUID();
        when(repository.findById(id)).thenReturn(Optional.empty());

        assertThrows(BusinessLogicException.class, () -> listService.findById(id));
    }

    @Test
    void shouldReturnListByItem() {
        ListItem item = new ListItem();
        when(repository.findByItemsContaining(item)).thenReturn(Optional.of(listEntity));

        var result = listService.findByItem(item);

        assertEquals(listEntity, result);
    }

    @Test
    void shouldThrow_whenItemNotFound() {
        ListItem item = new ListItem();
        when(repository.findByItemsContaining(item)).thenReturn(Optional.empty());

        assertThrows(BusinessLogicException.class, () -> listService.findByItem(item));
    }
}
