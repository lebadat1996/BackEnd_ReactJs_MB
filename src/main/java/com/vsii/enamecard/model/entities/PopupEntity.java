package com.vsii.enamecard.model.entities;

import javax.persistence.*;

@Entity
@Table(name = "popup")
public class PopupEntity extends AbsEntity<PopupEntity> {

    @Id
    @SequenceGenerator(name = "popup_id_seq",sequenceName = "popup_id_seq",allocationSize = 1, initialValue = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE,generator = "popup_id_seq")
    private Integer id;

    private String alternativeTitle;

    private String destinationUrl;

    private String avatar;

    private int channelId;

    private int priority;

    @Enumerated(value = EnumType.STRING)
    private BannerEntity.Status status;

    enum Status{
        INACTIVE,ACTIVE
    }

}
