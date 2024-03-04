package org.stepanov.telegram.bot.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.stepanov.telegram.bot.repository.EpRepository;
import org.stepanov.telegram.bot.repository.entity.EpEntity;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Log4j2
public class EpService {

    private final EpRepository epRepository;

    public void setBel(LocalDate date, Integer amount) {
        epRepository.saveBel(date, amount);
    }

    public void setGlobal(LocalDate date, Integer amount) {
        epRepository.saveGlobal(date, amount);
    }

    public Map<LocalDate, Integer> getBel() {
        return epRepository.findBel().stream()
                .collect(Collectors.toMap(EpEntity::date, EpEntity::number, (v1, v2) -> v1, TreeMap::new));
    }

    public Boolean deleteBelRecord(LocalDate date) {
        epRepository.deleteBel(date);
        return true;
    }

    public Boolean deleteGlobalRecord(LocalDate date) {
        epRepository.deleteGlobal(date);
        return true;
    }

//    public Map.Entry<LocalDate, Integer> getBel(LocalDate date) {
//        return getEntry(date, bel);
//    }

    public Map<LocalDate, Integer> getGlobal() {
        return epRepository.findGlobal().stream()
                .collect(Collectors.toMap(EpEntity::date, EpEntity::number, (v1, v2) -> v1, TreeMap::new));
    }

//    public Map.Entry<LocalDate, Integer> getGlobal(LocalDate date) {
//        return getEntry(date, global);
//    }

    private Map.Entry<LocalDate, Integer> getEntry(LocalDate date, TreeMap<LocalDate, Integer> collection) {
        if (collection.containsKey(date)) {
            return collection.ceilingEntry(date);
        }

        Map.Entry<LocalDate, Integer> ceilingEntry = collection.ceilingEntry(date);
        Map.Entry<LocalDate, Integer> floorEntry = collection.floorEntry(date);

        long betweenCeiling = ceilingEntry != null ? ChronoUnit.DAYS.between(date, ceilingEntry.getKey()) : Long.MAX_VALUE;
        long betweenFloor = floorEntry != null ? ChronoUnit.DAYS.between(date, floorEntry.getKey()) : Long.MAX_VALUE;

        return Math.abs(betweenFloor) <= Math.abs(betweenCeiling) ? floorEntry : ceilingEntry;
    }
}
