package com.vsii.enamecard.services;

import com.vsii.enamecard.model.dto.BannerDTO;
import com.vsii.enamecard.model.entities.BannerEntity;
import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

public interface GeneralService<T> {
    List<T> findAll(int page, int size);

    T findById(int id);

    void saveBanner(String alternativeTitle, String destinationUrl, MultipartFile file, int channelId, int priority, int creatorId, int modifierId, String status);

    void remove(int id);

    void updateBanner(int id, BannerDTO bannerDTO);

    Page<T> getAllBanner(int offset,int pageSize);

    Page<BannerDTO.infoBanner> getAllBanners(int offset,int pageSize);
}