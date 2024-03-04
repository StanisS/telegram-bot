package org.stepanov.telegram.bot.repository;

import org.stepanov.telegram.bot.repository.entity.EpEntity;

import java.time.LocalDate;
import java.util.List;

public interface EpRepository {

    List<EpEntity> findBel();
    List<EpEntity> findGlobal();

    EpEntity saveBel(LocalDate date, Integer number);
    EpEntity saveGlobal(LocalDate date, Integer number);

    void deleteBel(LocalDate date);
    void deleteGlobal(LocalDate date);
}
