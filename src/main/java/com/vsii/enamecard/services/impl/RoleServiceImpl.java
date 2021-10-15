package com.vsii.enamecard.services.impl;

import com.vsii.enamecard.exceptions.HttpErrorException;
import com.vsii.enamecard.model.entities.RoleEntity;
import com.vsii.enamecard.repositories.RoleRepository;
import com.vsii.enamecard.services.RoleService;
import org.springframework.stereotype.Service;

@Service
public class RoleServiceImpl implements RoleService {

    private final RoleRepository roleRepository;

    public RoleServiceImpl(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    public RoleEntity findByName(String name){
        return roleRepository.findByName(name);
    }

    @Override
    public RoleEntity findById(Integer id) {
        return roleRepository.findById(id).orElseThrow(() -> HttpErrorException.badRequest("role not exists"));
    }
}
