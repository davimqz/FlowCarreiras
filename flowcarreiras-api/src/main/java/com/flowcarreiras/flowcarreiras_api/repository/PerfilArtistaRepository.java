package com.flowcarreiras.flowcarreiras_api.repository;

import com.flowcarreiras.flowcarreiras_api.model.PerfilArtista;
import com.flowcarreiras.flowcarreiras_api.model.enums.StatusMentoria;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface PerfilArtistaRepository extends JpaRepository<PerfilArtista, UUID> {

    Optional<PerfilArtista> findByUsuarioEmail(String email);

    Optional<PerfilArtista> findByUrlPublica(String urlPublica);

    boolean existsByUrlPublica(String urlPublica);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select p from PerfilArtista p where p.id = :id")
    Optional<PerfilArtista> buscarPorIdComLock(@Param("id") UUID id);

    @Query("""
            select p from PerfilArtista p
            join p.usuario u
            where p.id <> :mentorId
              and u.ativo = true
              and p.onboardingConcluido = true
              and not exists (
                  select m.id from Mentoria m
                  where m.artista = p
                    and m.status = :statusAtivo
              )
            order by p.dataEntradaFila asc nulls last, u.nome asc
            """)
    List<PerfilArtista> listarArtistasSemMentoriaAtiva(
            @Param("mentorId") UUID mentorId,
            @Param("statusAtivo") StatusMentoria statusAtivo);

    @Query("""
            select p from PerfilArtista p
            join p.usuario u
            where u.ativo = true
              and p.perfilMentorConfigurado = true
              and p.disponivelParaMentorar = true
            order by u.nome asc
            """)
    List<PerfilArtista> listarMentoresAtivos();
}
