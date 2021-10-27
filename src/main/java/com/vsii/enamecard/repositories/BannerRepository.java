package com.vsii.enamecard.repositories;

import com.vsii.enamecard.model.dto.BannerDTO;
import com.vsii.enamecard.model.entities.BannerEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BannerRepository extends JpaRepository<BannerEntity, Integer> {
    @Query(value = "select b from BannerEntity b ORDER BY b.id asc ")
    List<BannerEntity> bannerList(Pageable pageable);

    @Query(value = "select b.id as id, b.avatar as avatar,b.alternativeTitle as alternativeTitle,b.priority as priority,b.destinationUrl as destinationUrl , b.status as status,ch.name as channelName  from BannerEntity b join ChannelEntity  ch on b.channelId= ch.id")
    Page<BannerDTO.infoBanner> listBanner(PageRequest pageRequest);
}
