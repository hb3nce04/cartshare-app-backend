package hu.unideb.cartshare.controller;

import hu.unideb.cartshare.dto.ListDto;
import hu.unideb.cartshare.service.ListService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/lists")
@RequiredArgsConstructor
public class ListController {
    private final ListService service;

    @GetMapping
    public ResponseEntity<List<ListDto>> findAll() {
        return ResponseEntity.ok(service.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ListDto> findById(@PathVariable String id) {
        return ResponseEntity.ok(service.findById(id));
    }
}
