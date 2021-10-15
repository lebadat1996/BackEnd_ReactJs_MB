package com.vsii.enamecard.model.entities;

import lombok.Data;
import javax.persistence.*;
import java.time.OffsetDateTime;

@Entity
@Table(name = "e_name_card")
@Data
public class ENameCardEntity extends AbsEntity<ENameCardEntity> {

    @Id
    @SequenceGenerator(name = "e_name_card_id_seq",sequenceName = "e_name_card_id_seq",allocationSize = 1, initialValue = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE,generator = "e_name_card_id_seq")
    private Integer id;

    private String avatar;

    private String codeAgent;

    private String email;

    private String facebookLink;

    private String fullName;

    private String phone;

    private int channelId;

    private String positions;

    @Enumerated(EnumType.STRING)
    private Status status;

    private String zaloLink;

    public enum Status {
        INACTIVE,ACTIVE
    }

}
