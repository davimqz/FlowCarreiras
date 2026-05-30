package com.flowcarreiras.flowcarreiras_api.repository;

import com.flowcarreiras.flowcarreiras_api.model.FilaDescobertaLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface FilaDescobertaLogRepository extends JpaRepository<FilaDescobertaLog, UUID> {
}
