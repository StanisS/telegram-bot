package org.stepanov.telegram.bot.repository;

import org.springframework.data.repository.CrudRepository;
import org.stepanov.telegram.bot.repository.entity.EpEntity;

import java.time.LocalDate;
import java.util.Collection;
import java.util.Optional;
import java.util.UUID;

public interface EpRepository extends CrudRepository<EpEntity, UUID> {

    Collection<EpEntity> findByScopeOrderByDate(String scope);

    Optional<EpEntity> findByScopeAndDate(String scope, LocalDate date);

    void deleteByScopeAndDate(String scope, LocalDate date);
}
