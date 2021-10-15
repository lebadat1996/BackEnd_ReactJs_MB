package com.vsii.enamecard.repositories;

import com.vsii.enamecard.model.entities.AccountEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountRepository extends JpaRepository<AccountEntity,Integer> {
    AccountEntity findByUsername(String username);

    AccountEntity findByUsernameAndPassword(String username, String password);

    boolean existsByUsernameAndPassword(String username, String password);

}
