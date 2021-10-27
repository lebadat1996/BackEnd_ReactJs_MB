package com.vsii.enamecard.services.impl;

import com.vsii.enamecard.model.dto.PopupDTO;
import com.vsii.enamecard.model.entities.BannerEntity;
import com.vsii.enamecard.model.entities.PopupEntity;
import com.vsii.enamecard.repositories.PopupRepository;
import com.vsii.enamecard.services.PopupService;
import org.aspectj.util.FileUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class PopupServiceImpl implements PopupService {
    private static final Logger log = LoggerFactory.getLogger(PopupServiceImpl.class);
    @Value("${mb.value.path}")
    String uploadBanner;
    @Autowired
    PopupRepository popupRepository;


    @Override
    public PopupEntity findById(int id) {
        log.info("Start Find Banner by id ");
        try {
            Optional<PopupEntity> popupEntity = popupRepository.findById(id);
            if (popupEntity.isPresent()) {
                log.info("Title: " + popupEntity.get().getAlternativeTitle());
                PopupEntity popup = popupEntity.get();
                byte[] fileContent = FileUtil.readAsByteArray(new File(popup.getAvatar()));
                String encodedString = Base64.getEncoder().encodeToString(fileContent);
                popup.setAvatar("data:image/png;base64," + encodedString);
                return popup;
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.error("Error Find Banner by id: ");
        }
        return null;
    }

    @Override
    public void savePopup(String alternativeTitle, String destinationUrl, MultipartFile file, int channelId, int priority, int creatorId, int modifierId, String status) {
        Date date = new Date();
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        String strDate = dateFormat.format(date);
        log.info("Create new Popup: " + strDate);
        try {
            String fileUpload = uploadBanner;
            String fileName = "";
            if (file != null) {
                fileName = file.getOriginalFilename();
                FileCopyUtils.copy(file.getBytes(), new File(fileUpload + fileName));
            }
            PopupEntity popupEntity = new PopupEntity();
            popupEntity.setAlternativeTitle(alternativeTitle);
            popupEntity.setDestinationUrl(destinationUrl);
            popupEntity.setAvatar(fileUpload + fileName);
            popupEntity.setChannelId(channelId);
            popupEntity.setPriority(priority);
            popupEntity.setCreatorId(creatorId);
            popupEntity.setModifierId(modifierId);
            if (status.equals("true")) {
                popupEntity.setStatus(BannerEntity.Status.ACTIVE);
                log.info("Status:  " + BannerEntity.Status.ACTIVE);
            }
            if (status.equals("false")) {
                log.info("Status:  " + BannerEntity.Status.INACTIVE);
                popupEntity.setStatus(BannerEntity.Status.INACTIVE);
            }
            popupRepository.save(popupEntity);
            String endDate = dateFormat.format(date);
            log.info("End create new Pop-up: " + endDate);
        } catch (Exception e) {
            e.printStackTrace();
            String dateError = dateFormat.format(date);
            log.error("Error create new Pop-up: " + dateError);
        }
    }

    @Override
    public void remove(int id) {
        Date date = new Date();
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        String strDate = dateFormat.format(date);
        log.info("Remove Pop-up: " + strDate);
        try {
            Optional<PopupEntity> popupEntity = popupRepository.findById(id);
            popupEntity.ifPresent(entity -> popupRepository.delete(entity));
            String endDate = dateFormat.format(date);
            log.info("End Remove Pop-up: " + endDate);
        } catch (Exception e) {
            e.printStackTrace();
            String dateError = dateFormat.format(date);
            log.error("Error Remove Pop-up: " + dateError);
        }
    }

    @Override
    public void updatePopup(int id, PopupDTO popupDTO) {
        Date date = new Date();
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        String strDate = dateFormat.format(date);
        log.info("Update new popup: " + strDate);
        try {
            Optional<PopupEntity> popupEntity = popupRepository.findById(id);
            if (popupEntity.isPresent()) {
                PopupEntity popup = popupEntity.get();
                popup.setDestinationUrl(popupDTO.getDestinationUrl());
                popup.setAlternativeTitle(popupDTO.getAlternativeTitle());
                popup.setPriority(popupDTO.getPriority());
                String fileName = "";
                if (popupDTO.getFile() != null) {
                    fileName = popupDTO.getFile().getOriginalFilename();
                    FileCopyUtils.copy(popupDTO.getFile().getBytes(), new File(uploadBanner + fileName));
                }
                popup.setAvatar(uploadBanner + fileName);
                if (popupDTO.getStatus().equals("true")) {
                    popup.setStatus(BannerEntity.Status.ACTIVE);
                    log.info("Status:  " + BannerEntity.Status.ACTIVE);
                }
                if (popupDTO.getStatus().equals("false")) {
                    log.info("Status:  " + PopupEntity.Status.INACTIVE);
                    popup.setStatus(BannerEntity.Status.INACTIVE);
                }
                popup.setCreatorId(popupDTO.getCreatorId());
                popup.setModifierId(popupDTO.getCreatorId());
                popupRepository.save(popup);
                log.info("End Update new popup: " + strDate);
            }
        } catch (Exception e) {
            e.printStackTrace();
            String dateError = dateFormat.format(date);
            log.error("Error update new popup: " + dateError);
        }
    }

    @Override
    public Page<PopupEntity> getAllPopup(int offset, int pageSize) {
        Date date = new Date();
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        String strDate = dateFormat.format(date);
        log.info("Find All List Popup: " + strDate);
        Page<PopupEntity> popupEntities = null;
        try {
            popupEntities = popupRepository.findAll(PageRequest.of(offset - 1, pageSize).withSort(Sort.by("id")));
            String endDate = dateFormat.format(date);
            log.info("End Find All List Popup: " + endDate);
        } catch (Exception e) {
            e.printStackTrace();
            log.error(e.getMessage());
        }
        return popupEntities;
    }

    @Override
    public List<PopupEntity> findAll(int page, int size) {
        return null;
    }
}
