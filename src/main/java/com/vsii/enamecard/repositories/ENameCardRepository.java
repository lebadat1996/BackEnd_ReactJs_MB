package com.vsii.enamecard.repositories;

import com.vsii.enamecard.model.entities.ENameCardEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;


@Repository
@Transactional
public interface ENameCardRepository extends JpaRepository<ENameCardEntity,Integer>, JpaSpecificationExecutor<ENameCardEntity> {

    boolean existsByEmail(String email);

    boolean existsByPhone(String phone);
}
