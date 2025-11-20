package hu.unideb.cartshare.service;

import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.springframework.stereotype.Service;

import hu.unideb.cartshare.exception.BusinessLogicException;
import hu.unideb.cartshare.mapper.ListMapper;
import hu.unideb.cartshare.model.dto.request.ListRequestDto;
import hu.unideb.cartshare.model.dto.response.ListResponseDto;
import hu.unideb.cartshare.model.entity.ListItem;
import hu.unideb.cartshare.model.entity.ListMembership;
import hu.unideb.cartshare.model.enums.MembershipRole;
import hu.unideb.cartshare.repository.ListRepository;
import lombok.RequiredArgsConstructor;

/**
 * Handles list management business logic.
 */
@Service
@RequiredArgsConstructor
public class ListService {
    private final ListRepository repository;
    private final ListMapper mapper;

    private final ListMembershipService listMembershipService;

    /**
     * Finding all lists where the currently authenticated user is an OWNER.
     * @return {@link List} of {@link ListResponseDto}
     */
    public List<ListResponseDto> findAllOwnedLists() {
        return mapper.toDtoList(
                listMembershipService.findAllOwnership().stream().map(ListMembership::getList)
                        .toList());
    }

    /**
     * Finding all lists where the currently authenticated user is only a MEMBER.
     * @return {@link List} of {@link ListResponseDto}
     */
    public List<ListResponseDto> findAllAllJoinedLists() {
        return mapper.toDtoList(
                listMembershipService.findAllMembership().stream().map(ListMembership::getList)
                        .toList());
    }

    /**
     * Creates a new list in the DB as an OWNER.
     * @param dto {@link ListRequestDto} request DTO
     * @return {@link ListResponseDto} response DTO
     */
    public ListResponseDto create(ListRequestDto dto) {
        hu.unideb.cartshare.model.entity.List list = new hu.unideb.cartshare.model.entity.List();
        list.setName(dto.getName());
        list.setItems(Set.of());

        repository.save(list);
        listMembershipService.join(list, MembershipRole.OWNER);

        return mapper.toDto(list);
    }

    /**
     * Joining to a specific list as a MEMBER.
     * @param id {@link java.util.UUID} id
     * @return {@link ListResponseDto} response DTO
     */
    public ListResponseDto joinById(UUID id) {
        hu.unideb.cartshare.model.entity.List foundList = findById(id);

        listMembershipService.join(foundList, MembershipRole.MEMBER);

        return mapper.toDto(foundList);
    }

    /**
     * Updates a specific list from the DB as an OWNER.
     * @param id {@link java.util.UUID} id
     * @param dto {@link ListRequestDto} request DTO
     */
    public void update(
            UUID id,
            ListRequestDto dto) {
        hu.unideb.cartshare.model.entity.List foundList = findById(id);

        if (listMembershipService.isOwner(foundList)) {
            foundList.setName(dto.getName());

            repository.save(foundList);
        }
    }

    /**
     * Deletes a specific entry from the DB. (ListMembership M:N table)
     * @param id {@link java.util.UUID} id
     */
    public void leaveById(UUID id) {
        hu.unideb.cartshare.model.entity.List foundList = findById(id);

        listMembershipService.leave(foundList);
    }

    /**
     * Deletes a specific list from the DB as an OWNER.
     * @param id {@link java.util.UUID} id
     */
    public void delete(UUID id) {
        hu.unideb.cartshare.model.entity.List foundList = findById(id);

        if (listMembershipService.isOwner(foundList)) {
            repository.delete(foundList);
        }
    }

    /**
     * Finds the list in the DB by UUID.
     * @param id {@link java.util.UUID} id
     * @return {@link hu.unideb.cartshare.model.entity.List} list entity
     */
    public hu.unideb.cartshare.model.entity.List findById(UUID id) {
        return repository.findById(id).orElseThrow(() -> new BusinessLogicException("This list is already exists."));
    }

    /**
     * Finds the list in the DB by list item inside.
     * @param item {@link ListItem} list item
     * @return {@link hu.unideb.cartshare.model.entity.List} list entity
     */
    public hu.unideb.cartshare.model.entity.List findByItem(ListItem item) {
        return repository.findByItemsContaining(item).orElseThrow(() -> new BusinessLogicException("This list item is not in any list."));
    }
}
