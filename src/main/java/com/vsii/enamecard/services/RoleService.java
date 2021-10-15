package com.vsii.enamecard.services;


import com.vsii.enamecard.model.entities.RoleEntity;

public interface RoleService {

    RoleEntity findByName(String name);

    RoleEntity findById(Integer id);
}
