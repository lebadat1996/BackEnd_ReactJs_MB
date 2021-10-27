package com.vsii.enamecard.services;

import com.vsii.enamecard.model.dto.BannerDTO;
import com.vsii.enamecard.model.dto.PopupDTO;
import com.vsii.enamecard.model.entities.PopupEntity;
import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface PopupService {
    List<PopupEntity> findAll(int page, int size);

    PopupEntity findById(int id);

    void savePopup(String alternativeTitle, String destinationUrl, MultipartFile file, int channelId, int priority, int creatorId, int modifierId, String status);

    void remove(int id);

    void updatePopup(int id, PopupDTO popupDTO);

    Page<PopupEntity> getAllPopup(int offset, int pageSize);
}
