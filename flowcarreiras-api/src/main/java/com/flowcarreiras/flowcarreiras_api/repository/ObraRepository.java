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
import java.util.Set;
import java.util.UUID;

@Repository
public interface ObraRepository extends JpaRepository<Obra, UUID> {

    List<Obra> findByArtistaIdOrderByDataPublicacaoDesc(UUID artistaId);

    List<Obra> findByArtistaIdAndStatusOrderByDataPublicacaoDesc(UUID artistaId, StatusObra status);

    Optional<Obra> findByIdAndArtistaId(UUID id, UUID artistaId);

    @Query("SELECT DISTINCT o FROM Obra o JOIN o.tags t WHERE t IN :tags AND o.status = 'PUBLICADA'")
    List<Obra> findPublicadasByTagsIn(@Param("tags") Collection<Tag> tags);

        @Query("""
            select o from Obra o
            join o.artista a
            join o.tags t
            where o.status = 'PUBLICADA'
              and (:area is null or lower(a.areaArtisticaPrincipal) like lower(concat('%', :area, '%')))
              and (:formatosVazio = true or o.tipoMidia in :formatos)
              and (:inicio is null or o.dataPublicacao >= :inicio)
              and (:fim is null or o.dataPublicacao <= :fim)
              and (:tagsVazio = true or lower(t.nome) in :tags)
            group by o
            having (:tagsVazio = true or count(distinct t.id) = :totalTags)
            order by o.dataPublicacao desc
            """)
        List<Obra> explorarObras(
            @Param("area") String area,
            @Param("formatosVazio") boolean formatosVazio,
            @Param("formatos") Set<com.flowcarreiras.flowcarreiras_api.model.enums.TipoMidia> formatos,
            @Param("inicio") java.time.LocalDateTime inicio,
            @Param("fim") java.time.LocalDateTime fim,
            @Param("tagsVazio") boolean tagsVazio,
            @Param("tags") Set<String> tags,
            @Param("totalTags") long totalTags
        );
}
