package com.vsii.enamecard.repositories;

import com.vsii.enamecard.model.entities.RoleEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<RoleEntity,Integer> {

    RoleEntity findByName(String name);
}
