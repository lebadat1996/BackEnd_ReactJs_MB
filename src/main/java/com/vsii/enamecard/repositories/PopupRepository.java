package com.vsii.enamecard.repositories;

import com.vsii.enamecard.model.entities.PopupEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PopupRepository extends JpaRepository<PopupEntity, Integer> {
}
