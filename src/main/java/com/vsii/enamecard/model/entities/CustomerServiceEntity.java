package com.vsii.enamecard.model.entities;

import lombok.Data;

import javax.persistence.*;
import java.time.OffsetDateTime;

@Table(name = "customer_service")
@Entity
@Data
public class CustomerServiceEntity {

    @Id
    @SequenceGenerator(name = "e_name_card_id_seq",sequenceName = "e_name_card_id_seq",allocationSize = 1, initialValue = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE,generator = "e_name_card_id_seq")
    private Integer id;

    private String name;

    private String icon;

    private String link;

    @Column(columnDefinition = "timestamp default now()")
    private OffsetDateTime dateCreate;
}
