package com.flowcarreiras.flowcarreiras_api.repository;

import com.flowcarreiras.flowcarreiras_api.model.NotificacaoOportunidade;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Repository
public interface NotificacaoOportunidadeRepository extends JpaRepository<NotificacaoOportunidade, UUID> {

    List<NotificacaoOportunidade> findTop20ByPerfilIdOrderByDataCriacaoDesc(UUID perfilId);

    long countByPerfilIdAndLidaFalse(UUID perfilId);

    long countByPerfilIdAndDataCriacaoBetween(UUID perfilId, LocalDateTime inicio, LocalDateTime fim);

    boolean existsByPerfilIdAndOportunidadeId(UUID perfilId, UUID oportunidadeId);

    @Query("""
            select n from NotificacaoOportunidade n
            where n.perfil.id = :perfilId
            order by n.dataCriacao desc
            """)
    List<NotificacaoOportunidade> listarPorPerfil(@Param("perfilId") UUID perfilId);
}
