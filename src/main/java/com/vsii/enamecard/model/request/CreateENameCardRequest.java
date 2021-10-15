package com.vsii.enamecard.model.request;

import com.vsii.enamecard.anotation.FullName;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Data
@NoArgsConstructor
public class CreateENameCardRequest {

    private String avatar;

    @NotBlank
    @NotNull
    @FullName
    private String fullName;

    @Pattern(regexp = "(0[3|5|7|8|9])+([0-9]{8}$)|(84[3|5|7|8|9])+([0-9]{8}$)",message = "this phone is incorrect format")
    @NotNull
    @NotBlank
    private String phone;

    @Email(regexp = "^([a-zA-Z0-9]+(?:[._+-][a-zA-Z0-9]+)*)@([a-zA-Z0-9]+(?:[.-][a-zA-Z0-9]+)*[.][a-zA-Z]{2,}$)")
    @NotNull
    @NotBlank
    private String email;

    @NotBlank
    private String facebookLink;

    private String positions;

}
