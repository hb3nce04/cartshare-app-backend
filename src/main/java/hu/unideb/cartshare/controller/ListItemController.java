package hu.unideb.cartshare.controller;

import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import hu.unideb.cartshare.model.dto.request.CreateListItemRequestDto;
import hu.unideb.cartshare.model.dto.request.UpdateListItemRequestDto;
import hu.unideb.cartshare.model.dto.response.ListItemResponseDto;
import hu.unideb.cartshare.service.ListItemService;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/list-items")
@RequiredArgsConstructor
public class ListItemController {
    private final ListItemService service;

    @PostMapping("/add")
    public ResponseEntity<ListItemResponseDto> create(@RequestBody @Validated CreateListItemRequestDto dto) {
        return ResponseEntity.ok(service.create(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ListItemResponseDto> update(@PathVariable @Validated UUID id, @RequestBody @Validated UpdateListItemRequestDto dto) {
        return ResponseEntity.ok(service.update(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ListItemResponseDto> delete(@PathVariable @Validated UUID id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
