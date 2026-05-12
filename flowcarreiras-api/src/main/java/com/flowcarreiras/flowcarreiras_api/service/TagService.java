package com.flowcarreiras.flowcarreiras_api.service;

import com.flowcarreiras.flowcarreiras_api.dto.TagDTO;
import com.flowcarreiras.flowcarreiras_api.repository.TagRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TagService {

    private final TagRepository tagRepository;

    public List<TagDTO> listarTodas() {
        return tagRepository.findAll()
                .stream()
                .map(TagDTO::from)
                .toList();
    }

    public List<TagDTO> buscarPorNome(String query) {
        if (query == null || query.isBlank()) return listarTodas();
        return tagRepository.findByNomeContainingIgnoreCase(query.trim())
                .stream()
                .map(TagDTO::from)
                .toList();
    }
}
