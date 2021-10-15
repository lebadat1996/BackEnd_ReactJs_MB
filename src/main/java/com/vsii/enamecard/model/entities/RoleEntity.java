package com.vsii.enamecard.model.entities;

import lombok.Data;

import javax.persistence.*;

@Entity
@Table(name = "role")
@Data
public class RoleEntity {

    @Id
    @SequenceGenerator(name = "role_id_seq",sequenceName = "role_id_seq",allocationSize = 1, initialValue = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE,generator = "role_id_seq")
    private Integer id;

    @Column(unique=true)
    private String name;

    private int channelId;
}
