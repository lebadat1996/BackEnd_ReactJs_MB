package com.vsii.enamecard.repositories;

import com.vsii.enamecard.model.entities.BannerEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BannerRepository extends CrudRepository<BannerEntity, Integer> {
    @Query(value = "select b from BannerEntity b ORDER BY b.id asc ")
    List<BannerEntity> bannerList();
}
