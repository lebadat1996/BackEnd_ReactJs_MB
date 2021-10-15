package com.vsii.enamecard.model.entities;

import lombok.Data;

import javax.persistence.*;
import java.time.OffsetDateTime;


@Entity
@Table(name = "account")
@Data
public class AccountEntity extends AbsEntity<AccountEntity> {
    @Id
    @SequenceGenerator(name = "account_id_seq",sequenceName = "account_id_seq",allocationSize = 1, initialValue = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE,generator = "account_id_seq")
    private Integer id;

    @Column(unique = true)
    private String username;

    private String password;

    private boolean firstLogin;

    private int eNameCardId;

    private int roleId;

    private String email;

    @Enumerated
    private Status status;

    public enum Status {
        INACTIVE,ACTIVE
    }
}
