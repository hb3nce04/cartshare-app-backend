package hu.unideb.cartshare.service;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;

import hu.unideb.cartshare.dto.response.ListResponseDto;
import hu.unideb.cartshare.mapper.ListMapper;
import hu.unideb.cartshare.repository.ListRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ListService {
    private final ListRepository repository;
    private final ListMapper mapper;

    private static final UUID USER_ID = UUID.fromString("6556437c-2a30-4e10-bfee-7239c5fc9e12");

    public List<ListResponseDto> findAll() {
        System.out.println(repository.findAllByUser_Id(USER_ID));
        return mapper.toDto(repository.findAllByUser_Id(USER_ID));
    }
}
