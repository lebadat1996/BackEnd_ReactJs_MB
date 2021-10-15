package com.vsii.enamecard.model.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class AccountDTO {

    private String email;
    private String username;
    private String password;
}
