package hu.unideb.cartshare.controller;

import hu.unideb.cartshare.dto.ListDto;
import hu.unideb.cartshare.service.ListService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/lists")
@RequiredArgsConstructor
public class ListController {
    private final ListService service;

    @PostMapping
    public ResponseEntity<ListDto> create(@RequestBody @Validated ListDto listDto) {
        return ResponseEntity.ok(service.create(listDto));
    }

    @GetMapping
    public ResponseEntity<List<ListDto>> findAll() {
        return ResponseEntity.ok(service.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ListDto> findById(@PathVariable String id) {
        return ResponseEntity.ok(service.findById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ListDto> updateById(@PathVariable String id, @RequestBody @Validated ListDto listDto) {
        return ResponseEntity.ok(service.updateById(id, listDto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable String id) {
        return ResponseEntity.ok(service.deleteById(id));
    }
}
