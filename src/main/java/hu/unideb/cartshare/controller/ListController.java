package hu.unideb.cartshare.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import hu.unideb.cartshare.model.dto.request.ListRequestDto;
import hu.unideb.cartshare.model.dto.response.ListResponseDto;
import hu.unideb.cartshare.service.ListService;
import lombok.RequiredArgsConstructor;

/**
 * Endpoints for managing lists and their business logic. It handles
 * <ul>
 *     <li>Getting the currently authenticated user's owned lists with their items</li>
 *     <li>Getting the currently authenticated user's joined lists with their items</li>
 *     <li>Creating a new list with an owner of the currently authenticated user</li>
 *     <li>Joining to an existing list</li>
 *     <li>Updating a currently authenticated user's joined list</li>
 *     <li>Leaving from a currently authenticated user's joined list</li>
 *     <li>Deleting a currently authenticated user's owned list</li>
 *  </ul>
 */
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

    @PostMapping("/new")
    public ResponseEntity<ListResponseDto> create(@RequestBody @Validated ListRequestDto dto) {
        return ResponseEntity.ok(service.create(dto));
    }

    @PostMapping("/join/{id}")
    public ResponseEntity<ListResponseDto> joinById(@PathVariable @Validated UUID id) {
        return ResponseEntity.ok(service.joinById(id));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<ListResponseDto> update(@PathVariable @Validated UUID id, @RequestBody @Validated ListRequestDto dto) {
        return ResponseEntity.ok(service.update(id, dto));
    }

    @DeleteMapping("/leave/{id}")
    public ResponseEntity<ListResponseDto> leaveById(@PathVariable @Validated UUID id) {
        return ResponseEntity.ok(service.leaveById(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable @Validated UUID id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
