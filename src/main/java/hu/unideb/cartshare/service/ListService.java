package hu.unideb.cartshare.service;

import hu.unideb.cartshare.dto.ListDto;
import hu.unideb.cartshare.dto.ListItemDto;
import hu.unideb.cartshare.exception.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ListService {
    private final List<ListDto> list = List.of(
            ListDto.builder().id("1").name("list1").items(
                    List.of(
                            ListItemDto.builder().id("1").name("todo1").isChecked(false).build()
                    )
            ).build()
    );

    public List<ListDto> findAll() {
        return list;
    }

    public ListDto findById(String id) {
        return list.stream().filter(l -> l.getId().equals(id)).findFirst().orElseThrow(() -> new EntityNotFoundException(
                String.format("Az adott entitás (id: %s) nem található!", id)
        ));
    }
}
