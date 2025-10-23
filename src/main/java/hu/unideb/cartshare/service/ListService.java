package hu.unideb.cartshare.service;

import java.util.List;
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

@Service
@RequiredArgsConstructor
public class ListService {
    private final ListRepository repository;
    private final ListMapper mapper;

    private final ListMembershipService listMembershipService;

    public List<ListResponseDto> findAllOwnedLists() {
        return mapper.toDtoList(
                listMembershipService.findAllOwnership().stream().map(ListMembership::getList)
                        .toList());
    }

    public List<ListResponseDto> findAllAllJoinedLists() {
        return mapper.toDtoList(
                listMembershipService.findAllMembership().stream().map(ListMembership::getList)
                        .toList());
    }

    public ListResponseDto create(ListRequestDto dto) {
        hu.unideb.cartshare.model.entity.List list = new hu.unideb.cartshare.model.entity.List();
        list.setName(dto.getName());

        repository.save(list);
        listMembershipService.join(list, MembershipRole.OWNER);

        return mapper.toDto(list);
    }

    public ListResponseDto joinById(UUID id) {
        hu.unideb.cartshare.model.entity.List foundList = findById(id);

        listMembershipService.join(foundList, MembershipRole.MEMBER);

        return mapper.toDto(foundList);
    }

    public ListResponseDto update(
            UUID id,
            ListRequestDto dto) {
        hu.unideb.cartshare.model.entity.List foundList = findById(id);

        if (listMembershipService.isOwner(foundList)) {
            foundList.setName(dto.getName());

            repository.save(foundList);
        }

        return mapper.toDto(foundList);
    }

    public ListResponseDto leaveById(UUID id) {
        hu.unideb.cartshare.model.entity.List foundList = findById(id);

        listMembershipService.leave(foundList);

        return mapper.toDto(foundList);
    }

    public void delete(UUID id) {
        hu.unideb.cartshare.model.entity.List foundList = findById(id);

        if (listMembershipService.isOwner(foundList)) {
            repository.delete(foundList);
        }
    }

    public hu.unideb.cartshare.model.entity.List findById(UUID id) {
        return repository.findById(id).orElseThrow(() -> new BusinessLogicException("Ez a lista nem létezik!"));
    }

    public hu.unideb.cartshare.model.entity.List findByItem(ListItem item) {
        return repository.findByItemsContaining(item).orElseThrow(() -> new BusinessLogicException("Ehhez az elemhez nem tartozik lista!"));
    }
}
