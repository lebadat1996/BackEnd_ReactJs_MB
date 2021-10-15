package com.vsii.enamecard.model.entities;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.*;


@Table(name = "banner")
@Entity
@Data
public class BannerEntity extends AbsEntity<BannerEntity> {

    @Id
    @SequenceGenerator(name = "banner_id_seq", sequenceName = "banner_id_seq", allocationSize = 1, initialValue = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "banner_id_seq")
    private Integer id;

    private String alternativeTitle;

    private String destinationUrl;

    private String avatar;

    private int channelId;

    private int priority;

    @Enumerated(value = EnumType.STRING)
    private Status status;

    public enum Status {
        INACTIVE, ACTIVE
    }
}
