package org.stepanov.telegram.bot.repository;

import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;
import org.stepanov.telegram.bot.repository.entity.EpEntity;
import org.stepanov.telegram.bot.repository.entity.Scope;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.stepanov.telegram.bot.repository.entity.Scope.BEL;
import static org.stepanov.telegram.bot.repository.entity.Scope.GLOBAL;

@Component
@Log4j2
public class EpRepositoryFileImpl implements EpRepository {

    private final List<EpEntity> epEntities = new ArrayList<>();
    private Boolean saveToFile = false;
    private Path path;

    public EpRepositoryFileImpl() {
    }

    public EpRepositoryFileImpl(List<EpEntity> epEntities) {
        this.epEntities.addAll(epEntities);
    }

    public EpRepositoryFileImpl(String filePath) throws IOException {
        saveToFile = true;
        path = Path.of(filePath);
        if (!Files.exists(path)) {
            Files.createFile(path);
        }
        BufferedReader br = Files.newBufferedReader(path, StandardCharsets.UTF_8);
        String line = br.readLine();
        while (line != null) {
            String[] attributes = line.split(",");
            EpEntity entity = EpEntity.builder()
                    .id(attributes[0] == null || attributes[0].isBlank() ? null : Long.valueOf(attributes[0]))
                    .scope(attributes[1] == null || attributes[1].isBlank() ? null : Scope.valueOf(attributes[1]))
                    .date(attributes[2] == null || attributes[2].isBlank() ? null : LocalDate.parse(attributes[2]))
                    .number(attributes[3] == null || attributes[3].isBlank() ? null : Integer.valueOf(attributes[3]))
                    .build();
            epEntities.add(entity);
            line = br.readLine();
        }
    }

    @Override
    public List<EpEntity> findBel() {
        return epEntities.stream()
                .filter(entity -> BEL.equals(entity.scope()))
                .toList();
    }

    @Override
    public List<EpEntity> findGlobal() {
        return epEntities.stream()
                .filter(entity -> GLOBAL.equals(entity.scope()))
                .toList();
    }

    @Override
    public EpEntity saveBel(LocalDate date, Integer number) {
        return saveEntity(date, number, BEL);
    }

    @Override
    public EpEntity saveGlobal(LocalDate date, Integer number) {
        return saveEntity(date, number, GLOBAL);
    }

    @Override
    public void deleteBel(LocalDate date) {
        epEntities.removeIf(entity -> BEL.equals(entity.scope()) && date.equals(entity.date()));
    }

    @Override
    public void deleteGlobal(LocalDate date) {
        epEntities.removeIf(entity -> GLOBAL.equals(entity.scope()) && date.equals(entity.date()));
    }

    private EpEntity saveEntity(LocalDate date, Integer number, Scope scope) {
        EpEntity entity = EpEntity.builder()
                .scope(scope)
                .date(date)
                .number(number)
                .build();
        if (saveToFile && !epEntities.contains(entity)) {
            try {
                Files.writeString(path, "\n" + entity.toCSVString(), StandardOpenOption.APPEND);
            } catch (IOException e) {
                log.error(e);
            }
        }
        epEntities.add(entity);
        return entity;
    }
}
