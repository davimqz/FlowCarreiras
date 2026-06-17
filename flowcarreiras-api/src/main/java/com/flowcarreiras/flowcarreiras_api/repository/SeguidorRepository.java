package com.flowcarreiras.flowcarreiras_api.repository;

import com.flowcarreiras.flowcarreiras_api.model.Seguidor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface SeguidorRepository extends JpaRepository<Seguidor, UUID> {

    // Quantos usuários seguem este perfil
    long countBySeguidoId(UUID seguidoId);

    // Quantos perfis este usuário segue
    long countBySeguidorId(UUID seguidorId);

    boolean existsBySeguidorIdAndSeguidoId(UUID seguidorId, UUID seguidoId);

    void deleteBySeguidorIdAndSeguidoId(UUID seguidorId, UUID seguidoId);
}
