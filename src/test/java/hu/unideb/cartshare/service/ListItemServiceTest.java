package hu.unideb.cartshare.service;

import hu.unideb.cartshare.exception.BusinessLogicException;
import hu.unideb.cartshare.mapper.ListItemMapper;
import hu.unideb.cartshare.model.dto.request.CreateListItemRequestDto;
import hu.unideb.cartshare.model.dto.request.UpdateListItemRequestDto;
import hu.unideb.cartshare.model.dto.response.ListItemResponseDto;
import hu.unideb.cartshare.model.entity.ListItem;
import hu.unideb.cartshare.repository.ListItemRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ListItemServiceTest {

    @Mock
    private ListItemRepository repository;

    @Mock
    private ListItemMapper mapper;

    @Mock
    private ListMembershipService listMembershipService;

    @Mock
    private ListService listService;

    @InjectMocks
    private ListItemService listItemService;

    private CreateListItemRequestDto createDto;
    private UpdateListItemRequestDto updateDto;
    private ListItem listItem;
    private ListItemResponseDto response;
    private hu.unideb.cartshare.model.entity.List mockList;
    private UUID id;

    @BeforeEach
    void setUp() {
        id = UUID.randomUUID();

        mockList = new hu.unideb.cartshare.model.entity.List();
        mockList.setId(UUID.randomUUID());

        listItem = new ListItem();
        listItem.setId(id);
        listItem.setName("Apples");
        listItem.setQuantity(5.0);
        listItem.setUnit("kg");
        listItem.setIsChecked(false);

        createDto = new CreateListItemRequestDto();
        createDto.setName("Apples");
        createDto.setQuantity(5.0);
        createDto.setUnit("kg");
        createDto.setListId(mockList.getId().toString());

        updateDto = new UpdateListItemRequestDto();
        updateDto.setName("Bananas");
        updateDto.setQuantity(3.0);
        updateDto.setUnit("pcs");
        updateDto.setIsChecked(true);

        response = new ListItemResponseDto();
        response.setName("Apples");
        response.setQuantity(5.0);
        response.setUnit("kg");
    }

    @Test
    void create_shouldSaveAndReturnDto_whenUserHasMembership() {
        when(listService.findById(UUID.fromString(createDto.getListId()))).thenReturn(mockList);
        when(listMembershipService.hasAnyMembershipInList(mockList)).thenReturn(true);
        when(mapper.toDto(any(ListItem.class))).thenReturn(response);

        ListItemResponseDto result = listItemService.create(createDto);

        assertNotNull(result);
        assertEquals("Apples", result.getName());
        verify(repository).save(any(ListItem.class));
    }

    @Test
    void create_shouldReturnNull_whenUserHasNoMembership() {
        when(listService.findById(any(UUID.class))).thenReturn(mockList);
        when(listMembershipService.hasAnyMembershipInList(mockList)).thenReturn(false);

        ListItemResponseDto result = listItemService.create(createDto);

        assertNull(result);
        verify(repository, never()).save(any());
    }

    @Test
    void update_shouldSaveAndReturnDto_whenUserHasMembership() {
        when(repository.findById(id)).thenReturn(Optional.of(listItem));
        when(listService.findByItem(listItem)).thenReturn(mockList);
        when(listMembershipService.hasAnyMembershipInList(mockList)).thenReturn(true);

        listItemService.update(id, updateDto);

        verify(repository).save(listItem);
    }

    @Test
    void update_shouldReturnNull_whenUserHasNoMembership() {
        when(repository.findById(id)).thenReturn(Optional.of(listItem));
        when(listService.findByItem(listItem)).thenReturn(mockList);
        when(listMembershipService.hasAnyMembershipInList(mockList)).thenReturn(false);

        listItemService.update(id, updateDto);

        verify(repository, never()).save(any());
    }

    @Test
    void delete_shouldCallRepositoryDelete_whenUserHasMembership() {
        when(repository.findById(id)).thenReturn(Optional.of(listItem));
        when(listService.findByItem(listItem)).thenReturn(mockList);
        when(listMembershipService.hasAnyMembershipInList(mockList)).thenReturn(true);

        listItemService.delete(id);

        verify(repository).delete(listItem);
    }

    @Test
    void delete_shouldNotCallRepositoryDelete_whenUserHasNoMembership() {
        when(repository.findById(id)).thenReturn(Optional.of(listItem));
        when(listService.findByItem(listItem)).thenReturn(mockList);
        when(listMembershipService.hasAnyMembershipInList(mockList)).thenReturn(false);

        listItemService.delete(id);

        verify(repository, never()).delete(any());
    }

    @Test
    void findById_shouldThrowException_whenNotFound() {
        when(repository.findById(id)).thenReturn(Optional.empty());

        assertThrows(BusinessLogicException.class, () -> listItemService.update(id, updateDto));
    }
}
