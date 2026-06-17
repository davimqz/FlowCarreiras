package com.flowcarreiras.flowcarreiras_api.repository;

import com.flowcarreiras.flowcarreiras_api.model.Curtida;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface CurtidaRepository extends JpaRepository<Curtida, UUID> {

    long countByObraId(UUID obraId);

    boolean existsByObraIdAndUsuarioId(UUID obraId, UUID usuarioId);

    void deleteByObraIdAndUsuarioId(UUID obraId, UUID usuarioId);

    void deleteByObraId(UUID obraId);
}
