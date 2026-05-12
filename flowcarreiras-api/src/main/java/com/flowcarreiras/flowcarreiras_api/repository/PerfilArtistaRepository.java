package com.flowcarreiras.flowcarreiras_api.repository;

import com.flowcarreiras.flowcarreiras_api.model.PerfilArtista;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface PerfilArtistaRepository extends JpaRepository<PerfilArtista, UUID> {

    Optional<PerfilArtista> findByUsuarioEmail(String email);

    Optional<PerfilArtista> findByUrlPublica(String urlPublica);

    boolean existsByUrlPublica(String urlPublica);
}
