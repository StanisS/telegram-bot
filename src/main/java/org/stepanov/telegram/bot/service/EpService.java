package org.stepanov.telegram.bot.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.stepanov.telegram.bot.repository.EpRepository;
import org.stepanov.telegram.bot.repository.entity.EpEntity;

import java.time.LocalDate;
import java.util.Map;
import java.util.TreeMap;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Log4j2
public class EpService {

    private final EpRepository epRepository;

    public void setAmount(String country, Integer amount, LocalDate date, Integer userId) {
        EpEntity entity = epRepository.findByScopeAndDate(country, date).orElse(new EpEntity(UUID.randomUUID()));
        entity.setScope(country);
        entity.setNumber(amount);
        entity.setDate(date);
        entity.setCreateBy(userId.toString());
        epRepository.save(entity);
    }

    public Map<LocalDate, Integer> getAmount(String country) {
        return epRepository.findByScopeOrderByDate(country).stream()
                .collect(Collectors.toMap(EpEntity::getDate, EpEntity::getNumber, (v1, v2) -> v1, TreeMap::new));
    }

    public void deleteRecord(String scope, LocalDate date) {
        epRepository.deleteByScopeAndDate(scope, date);
    }
}
