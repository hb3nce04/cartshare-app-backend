package hu.unideb.cartshare.service;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;

import hu.unideb.cartshare.model.dto.request.ListRequestDto;
import hu.unideb.cartshare.model.dto.response.ListResponseDto;
import hu.unideb.cartshare.mapper.ListMapper;
import hu.unideb.cartshare.model.entity.UserListMembership;
import hu.unideb.cartshare.model.enums.UserListRole;
import hu.unideb.cartshare.repository.UserListMembershipRepository;
import hu.unideb.cartshare.repository.ListRepository;
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
        userListMembershipRepository.save(membership);
        repository.save(list);
        return mapper.toDto(list);
    }

    public void delete(UUID id) {
        repository.deleteById(id);
    }

    public ListResponseDto joinById(UUID id) {
        return null;
    }
}
