package com.vsii.enamecard.model.entities;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import java.time.OffsetDateTime;

@MappedSuperclass
@JsonNaming(value = PropertyNamingStrategy.SnakeCaseStrategy.class)
@Data
public abstract class AbsEntity<T> {

    @CreationTimestamp
    @Column(columnDefinition = "timestamp default now()")
    private OffsetDateTime dateCreate;

    @UpdateTimestamp
    private OffsetDateTime dateModify;

    private int creatorId;

    private int modifierId;
}
