package com.flowcarreiras.flowcarreiras_api.repository;

import com.flowcarreiras.flowcarreiras_api.model.Mentoria;
import com.flowcarreiras.flowcarreiras_api.model.enums.StatusMentoria;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface MentoriaRepository extends JpaRepository<Mentoria, UUID> {

    boolean existsByArtistaIdAndStatus(UUID artistaId, StatusMentoria status);

    List<Mentoria> findByMentorIdOrderByDataCriacaoDesc(UUID mentorId);

    List<Mentoria> findByArtistaIdOrderByDataCriacaoDesc(UUID artistaId);

    Optional<Mentoria> findByIdAndMentorId(UUID id, UUID mentorId);
}
