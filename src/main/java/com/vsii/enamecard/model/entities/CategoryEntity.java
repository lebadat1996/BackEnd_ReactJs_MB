package com.vsii.enamecard.model.entities;

import lombok.Data;

import javax.persistence.*;

@Table(name = "category")
@Entity
@Data
public class CategoryEntity {

    @Id
    @SequenceGenerator(name = "category_id_seq",sequenceName = "category_id_seq",allocationSize = 1, initialValue = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE,generator = "category_id_seq")
    private Integer id;

    private String name;
}
