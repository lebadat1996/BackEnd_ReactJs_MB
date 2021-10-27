package com.vsii.enamecard.repositories;

import com.vsii.enamecard.model.entities.ENameCardEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Repository
@Transactional
public interface ENameCardRepository extends JpaRepository<ENameCardEntity, Integer>, JpaSpecificationExecutor<ENameCardEntity> {

    boolean existsByEmail(String email);

    boolean existsByPhone(String phone);

    @Query(value = "select e from ENameCardEntity e where (e.codeAgent is null or e.codeAgent like :codeAgent) and (e.fullName is null or e.fullName like :fullName) and (e.phone is null or e.phone like :phone)")
    List<ENameCardEntity> getAllENameCard(String codeAgent, String fullName, String phone);
}
