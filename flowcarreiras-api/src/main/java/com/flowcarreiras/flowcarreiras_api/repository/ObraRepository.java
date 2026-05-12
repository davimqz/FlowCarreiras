package com.flowcarreiras.flowcarreiras_api.repository;

import com.flowcarreiras.flowcarreiras_api.model.Obra;
import com.flowcarreiras.flowcarreiras_api.model.Tag;
import com.flowcarreiras.flowcarreiras_api.model.enums.StatusObra;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ObraRepository extends JpaRepository<Obra, UUID> {

    List<Obra> findByArtistaIdOrderByDataPublicacaoDesc(UUID artistaId);

    List<Obra> findByArtistaIdAndStatusOrderByDataPublicacaoDesc(UUID artistaId, StatusObra status);

    Optional<Obra> findByIdAndArtistaId(UUID id, UUID artistaId);

    @Query("SELECT DISTINCT o FROM Obra o JOIN o.tags t WHERE t IN :tags AND o.status = 'PUBLICADA'")
    List<Obra> findPublicadasByTagsIn(@Param("tags") Collection<Tag> tags);
}
