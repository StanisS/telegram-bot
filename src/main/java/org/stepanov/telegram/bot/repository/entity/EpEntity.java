package org.stepanov.telegram.bot.repository.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.io.Serializable;
import java.time.Instant;
import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "ep")
@Getter
@Setter
@EqualsAndHashCode(of = "epId")
@NoArgsConstructor
@AllArgsConstructor
public class EpEntity implements Serializable {
    @Id
    private UUID epId;
    private String scope;
    private LocalDate date;
    private Integer number;
    @CreationTimestamp
    private Instant createAt;
    private String createBy;

    public EpEntity(UUID epId) {
        this.epId = epId;
    }
}
