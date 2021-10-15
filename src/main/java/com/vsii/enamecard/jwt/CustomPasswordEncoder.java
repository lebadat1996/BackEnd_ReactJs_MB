package com.vsii.enamecard.jwt;

import com.vsii.enamecard.utils.EncryptUtils;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class CustomPasswordEncoder implements PasswordEncoder {

    @Override
    public String encode(CharSequence charSequence) {
        return EncryptUtils.crc32PasswordEncoder(charSequence.toString());
    }

    @Override
    public boolean matches(CharSequence charSequence, String s) {
        return s.equals(EncryptUtils.crc32PasswordEncoder(charSequence.toString()));
    }

}
