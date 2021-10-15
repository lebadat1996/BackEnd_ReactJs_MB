package com.vsii.enamecard.jwt.service;

import com.vsii.enamecard.jwt.model.CustomUserDetails;
import com.vsii.enamecard.model.entities.AccountEntity;
import com.vsii.enamecard.repositories.AccountRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

@Component
public class UserJwtService implements UserDetailsService {

    private final AccountRepository accountRepository;

    public UserJwtService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        AccountEntity accountEntity =  accountRepository.findByUsername(username);

        if (accountEntity == null){
            throw new UsernameNotFoundException(username);
        }
        return new CustomUserDetails(accountEntity);
    }

    public CustomUserDetails loadUserByUserId(Integer userId) {
        AccountEntity accountEntity = accountRepository.getById(userId);
        if (accountEntity == null) {
            throw new UsernameNotFoundException("userId" + userId);
        } else return new CustomUserDetails(accountEntity);
    }


}
