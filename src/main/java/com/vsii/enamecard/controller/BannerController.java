package com.vsii.enamecard.controller;

import com.vsii.enamecard.model.dto.BannerDTO;
import com.vsii.enamecard.model.entities.BannerEntity;
import com.vsii.enamecard.services.impl.BannerServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.vsii.enamecard.model.response.SystemResponse;

@RestController
@CrossOrigin("*")
@RequestMapping("/api/banner")
public class BannerController {
    @Autowired
    BannerServiceImpl bannerService;

    @PostMapping(value = "/save")
    public ResponseEntity<?> saveBanner(@ModelAttribute BannerDTO bannerDTO) {
        try {
            bannerService.saveBanner(bannerDTO.getAlternativeTitle(), bannerDTO.getDestinationUrl(), bannerDTO.getFile(), bannerDTO.getChannelId(), bannerDTO.getPriority(), bannerDTO.getCreatorId(), bannerDTO.getModifierId(), bannerDTO.getStatus());
            return new ResponseEntity("Successfully uploaded and create Banner!", HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>("Error uploaded and create Banner not success!", HttpStatus.BAD_REQUEST);
        }

    }

    @PostMapping(value = "/update/{id}")
    public ResponseEntity<?> updateBanner(@PathVariable int id, @ModelAttribute BannerDTO bannerDTO) {
        try {
            bannerService.updateBanner(id, bannerDTO);
            return new ResponseEntity("Successfully uploaded and update Banner!", HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>("Error uploaded and update Banner not success!", HttpStatus.BAD_REQUEST);
        }

    }

    @GetMapping(value = "/view")
    public ResponseEntity<?> bannerList() {
        try {
            return new ResponseEntity<>(bannerService.findAll(), HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>("Lỗi hiển thị danh sách banner", HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping(value = "/remove/{id}")
    public ResponseEntity<?> removeBanner(@PathVariable int id) {
        try {
            bannerService.remove(id);
            return new ResponseEntity<>("Xóa Banner thành công", HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>("Lỗi Xóa Banner banner", HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping(value = "/getBannerById/{id}")
    public ResponseEntity<?> getBannerById(@PathVariable int id) {
        try {
            BannerEntity bannerEntity = bannerService.findById(id);
            if (bannerEntity == null) {
                return new ResponseEntity<>(new SystemResponse(1000, "Không tìm thấy banner"), HttpStatus.OK);
            }
            return new ResponseEntity<>(new SystemResponse(1000, "Tìm kiếm banner thành công", bannerEntity), HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(new SystemResponse(1001, "Lỗi tìm kiếm banner"), HttpStatus.BAD_REQUEST);
        }
    }
}