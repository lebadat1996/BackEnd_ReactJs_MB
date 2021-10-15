package com.vsii.enamecard.model.request;

import lombok.Data;

@Data
public class ChangePasswordRequest {

    private String newPassword;
    private String oldPassword;
}
