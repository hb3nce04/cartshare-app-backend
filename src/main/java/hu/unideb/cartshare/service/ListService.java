package hu.unideb.cartshare.service;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;

import hu.unideb.cartshare.exception.BusinessLogicException;
import hu.unideb.cartshare.mapper.ListMapper;
import hu.unideb.cartshare.model.dto.request.ListRequestDto;
import hu.unideb.cartshare.model.dto.response.ListResponseDto;
import hu.unideb.cartshare.model.entity.UserListMembership;
import hu.unideb.cartshare.model.enums.UserListRole;
import hu.unideb.cartshare.repository.ListRepository;
import hu.unideb.cartshare.repository.UserListMembershipRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ListService {
    private final ListRepository repository;
    private final ListMapper mapper;
    private final UserService userService;
    private final UserListMembershipRepository userListMembershipRepository;

    private static final UUID USER_ID = UUID.fromString("6556437c-2a30-4e10-bfee-7239c5fc9e12");

    public List<ListResponseDto> findAllOwnedLists() {
        return mapper.toDtoList(userListMembershipRepository.findAllByUser_IdAndRoleIs(USER_ID, UserListRole.OWNER).stream().map(UserListMembership::getList).toList());
    }

    public List<ListResponseDto> findAllAllJoinedLists() {
        return mapper.toDtoList(userListMembershipRepository.findAllByUser_IdAndRoleIs(USER_ID, UserListRole.MEMBER).stream().map(UserListMembership::getList).toList());
    }

    public ListResponseDto create(ListRequestDto dto) {
        hu.unideb.cartshare.model.entity.List list = mapper.toEntity(dto);

        UserListMembership membership = new UserListMembership();
        membership.setList(list);
        membership.setUser(userService.findById(USER_ID));
        membership.setRole(UserListRole.OWNER);

        userListMembershipRepository.save(membership);
        repository.save(list);

        return mapper.toDto(list);
    }

    public ListResponseDto joinById(UUID id) {
        hu.unideb.cartshare.model.entity.List foundList = findById(id);

        if (userListMembershipRepository.existsByListAndUser_IdAndRoleIs(foundList, USER_ID, UserListRole.OWNER)) {
            throw new BusinessLogicException("Ennek a listának már tulajdonosa vagy!");
        }
        if (userListMembershipRepository.existsByListAndUser_IdAndRoleIs(foundList, USER_ID, UserListRole.MEMBER)) {
            throw new BusinessLogicException("Ennek a listának már tagja vagy!");
        }

        UserListMembership membership = new UserListMembership();
        membership.setList(foundList);
        membership.setUser(userService.findById(USER_ID));
        membership.setRole(UserListRole.MEMBER);
        // TODO: joinedAt ?
        userListMembershipRepository.save(membership);

        return mapper.toDto(foundList);
    }

    public ListResponseDto update(UUID id, ListRequestDto dto) {
        hu.unideb.cartshare.model.entity.List foundList = findById(id);

        foundList.setName(dto.getName());

        repository.save(foundList);

        return mapper.toDto(foundList);
    }

    public ListResponseDto leaveById(UUID id) {
        hu.unideb.cartshare.model.entity.List foundList = findById(id);

        if (!userListMembershipRepository.existsByListAndUser_IdAndRoleIs(foundList, USER_ID, UserListRole.MEMBER)) {
            throw new BusinessLogicException("Ennek a listának nem vagy a tagja!");
        }

        userListMembershipRepository.deleteByListAndUser_Id(foundList, USER_ID);

        return mapper.toDto(foundList);
    }

    public void delete(UUID id) {
        hu.unideb.cartshare.model.entity.List foundList = findById(id);

        if (!userListMembershipRepository.existsByListAndUser_IdAndRoleIs(foundList, USER_ID, UserListRole.OWNER)) {
            throw new BusinessLogicException("Ennek a listának nem vagy a tulajdonosa!");
        }

        repository.deleteById(id);
    }

    private hu.unideb.cartshare.model.entity.List findById(UUID id) {
        return repository.findById(id).orElseThrow(() -> new BusinessLogicException("Ez a lista nem létezik!"));
    }
}
