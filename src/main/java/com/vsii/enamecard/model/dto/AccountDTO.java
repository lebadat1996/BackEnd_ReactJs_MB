package com.vsii.enamecard.model.dto;

import com.vsii.enamecard.model.entities.AccountEntity;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class AccountDTO {
    private Integer id;

    private String username;

    private String password;

    private boolean firstLogin;

    private int eNameCardId;

    private int roleId;

    private String email;

    private AccountEntity.Status status;

    private Integer channelId;

    private String roleName;
}
