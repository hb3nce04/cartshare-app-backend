package hu.unideb.cartshare.controller;

import java.util.List;
import java.util.UUID;

import hu.unideb.cartshare.model.dto.request.ListRequestDto;
import hu.unideb.cartshare.model.dto.response.ListResponseDto;
import hu.unideb.cartshare.service.ListService;
import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/lists")
@RequiredArgsConstructor
public class ListController {
    private final ListService service;

    @GetMapping("/owned")
    public ResponseEntity<List<ListResponseDto>> findAllOwnedLists() {
        return ResponseEntity.ok(service.findAllOwnedLists());
    }

    @GetMapping("/joined")
    public ResponseEntity<List<ListResponseDto>> findAllAllJoinedLists() {
        return ResponseEntity.ok(service.findAllAllJoinedLists());
    }

    @PostMapping("/join")
    public ResponseEntity<ListResponseDto> joinById(@RequestParam @Validated UUID id) {
        return ResponseEntity.ok(service.joinById(id));
    }

    @PostMapping("/new")
    public ResponseEntity<ListResponseDto> create(@RequestBody @Validated ListRequestDto dto) {
        return ResponseEntity.ok(service.create(dto));
    }

    // TODO: authorization
    @DeleteMapping
    public ResponseEntity<Void> delete(@RequestBody @Validated UUID id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
