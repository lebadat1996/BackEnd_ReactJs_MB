package com.vsii.enamecard.model.entities;

import lombok.Data;

import javax.persistence.*;
import java.time.OffsetDateTime;

@Table(name = "enamecard_history")
@Entity
@Data
public class ENameCardHistoryEntity {

    @Id
    @SequenceGenerator(name = "enamecard_history_id_seq", sequenceName = "enamecard_history_id_seq",allocationSize = 1,initialValue = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE,generator = "enamecard_history_id_seq")
    private Integer id;

    private String fieldName;

    private String oldName;

    private String newValue;

    private int eNameCardId;

    private OffsetDateTime dateModify;

    private int modifier_id;

}
