package com.flowcarreiras.flowcarreiras_api.dto;

import com.flowcarreiras.flowcarreiras_api.model.Tag;
import com.flowcarreiras.flowcarreiras_api.model.enums.CategoriaTag;
import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
public class TagDTO {

    private UUID id;
    private String nome;
    private CategoriaTag categoria;

    public static TagDTO from(Tag tag) {
        return TagDTO.builder()
                .id(tag.getId())
                .nome(tag.getNome())
                .categoria(tag.getCategoria())
                .build();
    }
}
