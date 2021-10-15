package com.vsii.enamecard.configs;

import com.vsii.enamecard.model.entities.AccountEntity;
import com.vsii.enamecard.model.entities.RoleEntity;
import com.vsii.enamecard.repositories.AccountRepository;
import com.vsii.enamecard.repositories.RoleRepository;
import com.vsii.enamecard.utils.Constant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.OffsetDateTime;

@Component
public class DataSeedingListener implements ApplicationListener<ContextRefreshedEvent> {

    private final AccountRepository accountRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public DataSeedingListener(AccountRepository accountRepository,
                               RoleRepository roleRepository,
                               PasswordEncoder passwordEncoder) {
        this.accountRepository = accountRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {

        RoleEntity roleEntityAdmin = roleRepository.findByName(Constant.NAME_ROLE_ADMIN);
        RoleEntity roleEntityUser = roleRepository.findByName(Constant.ROLE_NAME_AGENT);
        if (roleEntityAdmin == null) {

            roleEntityAdmin = new RoleEntity();
            roleEntityAdmin.setName(Constant.NAME_ROLE_ADMIN);
            roleEntityAdmin = roleRepository.save(roleEntityAdmin);
        }
        if (roleEntityUser == null) {

            roleEntityUser = new RoleEntity();
            roleEntityUser.setName(Constant.ROLE_NAME_AGENT);
            roleRepository.save(roleEntityUser);
        }

        if (accountRepository.findByUsername("admin") == null) {
            AccountEntity accountEntity = new AccountEntity();
            accountEntity.setUsername("admin");
            accountEntity.setPassword( passwordEncoder.encode("admin"));
            accountEntity.setFirstLogin(false);
            accountEntity.setRoleId(roleEntityAdmin.getId());
            accountEntity.setDateCreate(OffsetDateTime.now());
            accountRepository.save(accountEntity);
        }
    }
}
