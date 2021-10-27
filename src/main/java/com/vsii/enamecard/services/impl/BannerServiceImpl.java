package com.vsii.enamecard.services.impl;

import com.vsii.enamecard.model.dto.BannerDTO;
import com.vsii.enamecard.model.entities.BannerEntity;
import com.vsii.enamecard.repositories.BannerRepository;
import com.vsii.enamecard.services.BannerService;
import org.aspectj.util.FileUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class BannerServiceImpl implements BannerService {
    private static final Logger log = LoggerFactory.getLogger(BannerServiceImpl.class);
    @Value("${mb.value.path}")
    String uploadBanner;

    @Autowired
    BannerRepository bannerRepository;

    @Override
    public List<BannerEntity> findAll(int page, int size) {
        Date date = new Date();
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        String strDate = dateFormat.format(date);
        List<BannerEntity> bannerEntityList = new ArrayList<>();
        log.info("Find All List Banner: " + strDate);
        try {
            Pageable pageable = PageRequest.of(page, size);
            Pageable paging = PageRequest.of(page, size, Sort.by("id"));
            Iterable<BannerEntity> bannerEntityIterable = bannerRepository.bannerList(pageable);
            for (BannerEntity bannerEntity : bannerEntityIterable) {
                bannerEntityList.add(bannerEntity);
            }
            String endDate = dateFormat.format(date);
            log.info("End Find All List Banner: " + endDate);
        } catch (Exception e) {
            e.printStackTrace();
            log.error(e.getMessage());
        }
        return bannerEntityList;
    }

    @Override
    public BannerEntity findById(int id) {
        log.info("Start Find Banner by id ");
        try {
            Optional<BannerEntity> bannerEntity = bannerRepository.findById(id);
            if (bannerEntity.isPresent()) {
                log.info("Title: " + bannerEntity.get().getAlternativeTitle());
                BannerEntity banner = bannerEntity.get();
                byte[] fileContent = FileUtil.readAsByteArray(new File(banner.getAvatar()));
                String encodedString = Base64.getEncoder().encodeToString(fileContent);
                banner.setAvatar("data:image/png;base64," + encodedString);
                return banner;
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.error("Error Find Banner by id: ");
        }
        return null;
    }

    @Override
    public void saveBanner(String alternativeTitle, String destinationUrl, MultipartFile file, int channelId, int priority, int creatorId, int modifierId, String status) {
        Date date = new Date();
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        String strDate = dateFormat.format(date);
        log.info("Create new Banner: " + strDate);
        try {
            String fileUpload = uploadBanner;
            String fileName = "";
            if (file != null) {
                fileName = file.getOriginalFilename();
                FileCopyUtils.copy(file.getBytes(), new File(fileUpload + fileName));
            }
            BannerEntity bannerEntity = new BannerEntity();
            bannerEntity.setAlternativeTitle(alternativeTitle);
            bannerEntity.setDestinationUrl(destinationUrl);
            bannerEntity.setAvatar(fileUpload + fileName);
            bannerEntity.setChannelId(channelId);
            bannerEntity.setPriority(priority);
            bannerEntity.setCreatorId(creatorId);
            bannerEntity.setCreatorId(modifierId);
            if (status.equals("true")) {
                bannerEntity.setStatus(BannerEntity.Status.ACTIVE);
                log.info("Status:  " + BannerEntity.Status.ACTIVE);
            }
            if (status.equals("false")) {
                log.info("Status:  " + BannerEntity.Status.INACTIVE);
                bannerEntity.setStatus(BannerEntity.Status.INACTIVE);
            }
            bannerRepository.save(bannerEntity);
            String endDate = dateFormat.format(date);
            log.info("End create new Banner: " + endDate);
        } catch (Exception e) {
            e.printStackTrace();
            String dateError = dateFormat.format(date);
            log.error("Error create new Banner: " + dateError);
        }
    }

    @Override
    public void remove(int id) {
        Date date = new Date();
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        String strDate = dateFormat.format(date);
        log.info("Remove Banner: " + strDate);
        try {
            Optional<BannerEntity> bannerEntity = bannerRepository.findById(id);
            bannerEntity.ifPresent(entity -> bannerRepository.delete(entity));
            String endDate = dateFormat.format(date);
            log.info("End Remove Banner: " + endDate);
        } catch (Exception e) {
            e.printStackTrace();
            String dateError = dateFormat.format(date);
            log.error("Error Remove Banner: " + dateError);
        }
    }

    @Override
    public void updateBanner(int id, BannerDTO bannerDTO) {
        Date date = new Date();
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        String strDate = dateFormat.format(date);
        log.info("Update new Banner: " + strDate);
        try {
            Optional<BannerEntity> bannerEntity = bannerRepository.findById(id);
            if (bannerEntity.isPresent()) {
                BannerEntity banner = bannerEntity.get();
                banner.setDestinationUrl(bannerDTO.getDestinationUrl());
                banner.setAlternativeTitle(bannerDTO.getAlternativeTitle());
                banner.setPriority(bannerDTO.getPriority());
                String fileName = "";
                if (bannerDTO.getFile() != null) {
                    fileName = bannerDTO.getFile().getOriginalFilename();
                    FileCopyUtils.copy(bannerDTO.getFile().getBytes(), new File(uploadBanner + fileName));
                }
                banner.setAvatar(uploadBanner + fileName);
                if (bannerDTO.getStatus().equals("true")) {
                    banner.setStatus(BannerEntity.Status.ACTIVE);
                    log.info("Status:  " + BannerEntity.Status.ACTIVE);
                }
                if (bannerDTO.getStatus().equals("false")) {
                    log.info("Status:  " + BannerEntity.Status.INACTIVE);
                    banner.setStatus(BannerEntity.Status.INACTIVE);
                }
                banner.setCreatorId(bannerDTO.getCreatorId());
                banner.setModifierId(bannerDTO.getCreatorId());
                bannerRepository.save(banner);
                log.info("End Update new Banner: " + strDate);
            }
        } catch (Exception e) {
            e.printStackTrace();
            String dateError = dateFormat.format(date);
            log.error("Error update new Banner: " + dateError);
        }
    }

    @Override
    public Page<BannerEntity> getAllBanner(int offset, int pageSize) {
        return null;
    }

    @Override
    public Page<BannerDTO.infoBanner> getAllBanners(int offset, int pageSize) {
        Date date = new Date();
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        String strDate = dateFormat.format(date);
        log.info("Find All List Banner: " + strDate);
        Page<BannerDTO.infoBanner> bannerEntityPage = null;
        try {
            bannerEntityPage = bannerRepository.listBanner(PageRequest.of(offset - 1, pageSize).withSort(Sort.by("id")));
            String endDate = dateFormat.format(date);
            log.info("End Find All List Banner: " + endDate);
        } catch (Exception e) {
            e.printStackTrace();
            log.error(e.getMessage());
        }
        return bannerEntityPage;
    }

}
