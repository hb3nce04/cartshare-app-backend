package hu.unideb.cartshare.service;

import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.springframework.stereotype.Service;

import hu.unideb.cartshare.exception.BusinessLogicException;
import hu.unideb.cartshare.mapper.ListItemMapper;
import hu.unideb.cartshare.model.dto.request.CreateListItemRequestDto;
import hu.unideb.cartshare.model.dto.request.UpdateListItemRequestDto;
import hu.unideb.cartshare.model.dto.response.ListItemResponseDto;
import hu.unideb.cartshare.model.entity.ListItem;
import hu.unideb.cartshare.repository.ListItemRepository;
import lombok.RequiredArgsConstructor;

/**
 * Handles list item management business logic.
 */
@Service
@RequiredArgsConstructor
public class ListItemService {
    private final ListItemRepository repository;
    private final ListItemMapper mapper;

    private final ListMembershipService listMembershipService;
    private final ListService listService;

    /**
     * Creates a new list item in the DB as a MEMBER or an OWNER.
     * @param dto {@link hu.unideb.cartshare.model.dto.request.CreateListItemRequestDto} request DTO
     * @return {@link hu.unideb.cartshare.model.dto.response.ListItemResponseDto} response DTO
     */
    public ListItemResponseDto create(CreateListItemRequestDto dto) {
        hu.unideb.cartshare.model.entity.List foundList = listService.findById(UUID.fromString(dto.getListId()));

        ListItem listItem = new ListItem();
        if (listMembershipService.hasAnyMembershipInList(foundList)) {
            listItem.setName(dto.getName());
            listItem.setQuantity(dto.getQuantity());
            listItem.setUnit(dto.getUnit());
            listItem.setList(foundList);

            repository.save(listItem);

            return mapper.toDto(listItem);
        }

        return null;
    }

    /**
     * Updates a specific list item in the DB as a MEMBER or an OWNER.
     * @param id {@link java.util.UUID} id
     * @param dto {@link hu.unideb.cartshare.model.dto.request.UpdateListItemRequestDto} request DTO
     */
    public void update(
            UUID id,
            UpdateListItemRequestDto dto) {
        ListItem foundListItem = findById(id);
        hu.unideb.cartshare.model.entity.List foundList = listService.findByItem(foundListItem);

        if (listMembershipService.hasAnyMembershipInList(foundList)) {
            foundListItem.setName(dto.getName());
            foundListItem.setQuantity(dto.getQuantity());
            foundListItem.setUnit(dto.getUnit());
            foundListItem.setIsChecked(dto.getIsChecked());

            repository.save(foundListItem);
        }
    }

    /**
     * Retrieves all list items that belong to the list identified by the given id.
     * @param id {@link java.util.UUID} id
     * @return {@link List} of {@link hu.unideb.cartshare.model.dto.response.ListItemResponseDto}
     */
    public Set<ListItemResponseDto> findItemsByListId(UUID id) {
        hu.unideb.cartshare.model.entity.List foundList = listService.findById(id);

        if (listMembershipService.hasAnyMembershipInList(foundList)) {
            return mapper.toDtoSet(foundList.getItems());
        }

        return null;
    }

    /**
     * Deletes a specific list item in the DB as a MEMBER or an OWNER.
     * @param id {@link java.util.UUID} id
     */
    public void delete(UUID id) {
        ListItem foundListItem = findById(id);
        hu.unideb.cartshare.model.entity.List foundList = listService.findByItem(foundListItem);

        if (listMembershipService.hasAnyMembershipInList(foundList)) {
            repository.delete(foundListItem);
        }
    }

    /**
     * Finds the list item in the DB by UUID.
     * @param id {@link java.util.UUID} id
     * @return {@link hu.unideb.cartshare.model.entity.ListItem} list item entity
     */
    private ListItem findById(UUID id) {
        return repository.findById(id).orElseThrow(() -> new BusinessLogicException("Ez a listaelem nem létezik."));
    }
}
