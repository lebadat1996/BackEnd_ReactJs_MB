package com.vsii.enamecard.model.entities;

import lombok.Data;

import javax.persistence.*;

@Table(name = "post")
@Entity
@Data
public class PostEntity extends AbsEntity<PostEntity> {

    @Id
    @SequenceGenerator(name = "post_id_seq", sequenceName = "post_id_seq", allocationSize = 1, initialValue = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "post_id_seq")
    private Integer id;

    private String title;

    private String avatar;

    @Column(columnDefinition = "text")
    private String content;

    private String keyVisual;

    private int channelId;

    private int priority;

    private int categoryId;

    @Enumerated(value = EnumType.STRING)
    private Status status;

     public enum Status {
        PENDING_APPROVAL, ACTIVE, INACTIVE
    }

}
