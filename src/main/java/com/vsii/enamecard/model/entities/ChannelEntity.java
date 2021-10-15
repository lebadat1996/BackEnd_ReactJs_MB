package com.vsii.enamecard.model.entities;

import javax.persistence.*;

@Table(name = "channel")
@Entity
public class ChannelEntity {

    @Id
    @SequenceGenerator(name = "channel_id_seq",sequenceName = "channel_id_seq",allocationSize = 1, initialValue = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE,generator = "channel_id_seq")
    private Integer id;

    @Column(unique = true)
    public String name;

}
