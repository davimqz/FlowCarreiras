package com.flowcarreiras.flowcarreiras_api.controller;

import com.flowcarreiras.flowcarreiras_api.dto.TagDTO;
import com.flowcarreiras.flowcarreiras_api.service.TagService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/tags")
@RequiredArgsConstructor
public class TagController {

    private final TagService tagService;

    @GetMapping
    ResponseEntity<List<TagDTO>> listarTodas() {
        return ResponseEntity.ok(tagService.listarTodas());
    }

    @GetMapping("/search")
    ResponseEntity<List<TagDTO>> buscar(@RequestParam(defaultValue = "") String q) {
        return ResponseEntity.ok(tagService.buscarPorNome(q));
    }
}
