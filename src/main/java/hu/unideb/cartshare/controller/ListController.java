package hu.unideb.cartshare.controller;

import java.util.List;

import hu.unideb.cartshare.dto.response.ListResponseDto;
import hu.unideb.cartshare.service.ListService;
import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/lists")
@RequiredArgsConstructor
public class ListController {
    private final ListService service;

    @GetMapping
    public ResponseEntity<List<ListResponseDto>> findAll() {
        return ResponseEntity.ok(service.findAll());
    }
}
