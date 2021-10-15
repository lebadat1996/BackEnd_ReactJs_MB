package com.vsii.enamecard.model.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginResponse {

    private Integer id;

    private String username;

    private boolean firstLogin;

    private OffsetDateTime createdAt;

    private int roleId;

    private String token;

    private String roleName;

    private int eNameCardId;

}
