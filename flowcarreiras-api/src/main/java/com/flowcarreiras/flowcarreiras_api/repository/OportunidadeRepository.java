package com.flowcarreiras.flowcarreiras_api.repository;

import com.flowcarreiras.flowcarreiras_api.model.Oportunidade;
import com.flowcarreiras.flowcarreiras_api.model.enums.OportunidadeStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Repository
public interface OportunidadeRepository extends JpaRepository<Oportunidade, UUID> {

    List<Oportunidade> findByStatusAndDataEncerramentoGreaterThanEqualOrderByDataEncerramentoAsc(
            OportunidadeStatus status,
            LocalDate data
    );

    @Modifying
    @Query("""
            update Oportunidade o
            set o.status = :statusExpirada
            where o.status = :statusAtiva
              and o.dataEncerramento < :hoje
            """)
    int expirarAntesDe(@Param("hoje") LocalDate hoje,
                      @Param("statusAtiva") OportunidadeStatus statusAtiva,
                      @Param("statusExpirada") OportunidadeStatus statusExpirada);
}
