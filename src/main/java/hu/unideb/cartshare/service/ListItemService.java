package hu.unideb.cartshare.service;

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

@Service
@RequiredArgsConstructor
public class ListItemService {
    private final ListItemRepository repository;
    private final ListItemMapper mapper;

    private final ListMembershipService listMembershipService;
    private final ListService listService;

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

    public ListItemResponseDto update(
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

            return mapper.toDto(foundListItem);
        }

        return null;
    }

    public void delete(UUID id) {
        ListItem foundListItem = findById(id);
        hu.unideb.cartshare.model.entity.List foundList = listService.findByItem(foundListItem);

        if (listMembershipService.hasAnyMembershipInList(foundList)) {
            repository.delete(foundListItem);
        }
    }

    private ListItem findById(UUID id) {
        return repository.findById(id).orElseThrow(() -> new BusinessLogicException("Ez a listaelem nem létezik!"));
    }
}
