package com.vsii.enamecard.controller;

import com.vsii.enamecard.model.dto.PopupDTO;
import com.vsii.enamecard.model.entities.PopupEntity;
import com.vsii.enamecard.model.response.SystemResponse;
import com.vsii.enamecard.services.impl.PopupServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin("*")
@RequestMapping(value = "/api/popup")
public class PopupController {
    @Autowired
    PopupServiceImpl popupService;


    @PostMapping(value = "/save")
    public ResponseEntity<?> savePopup(@ModelAttribute PopupDTO popupDTO) {
        try {
            popupService.savePopup(popupDTO.getAlternativeTitle(), popupDTO.getDestinationUrl(), popupDTO.getFile(), popupDTO.getChannelId(), popupDTO.getPriority(), popupDTO.getCreatorId(), popupDTO.getModifierId(), popupDTO.getStatus());
            return new ResponseEntity(new SystemResponse(1000, "Successfully uploaded and create Banner!"), HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(new SystemResponse(1001, "Error uploaded and create Banner not success!"), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping(value = "/view")
    public ResponseEntity<Page<PopupEntity>> popupList(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "5") int size) {
        try {
            return new ResponseEntity<>(popupService.getAllPopup(page, size), HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping(value = "/getPopupById/{id}")
    public ResponseEntity<?> getPopupById(@PathVariable int id) {
        try {
            PopupEntity popupEntity = popupService.findById(id);
            if (popupEntity == null) {
                return new ResponseEntity<>(new SystemResponse(1000, "Không tìm thấy popup"), HttpStatus.OK);
            } else {
                return new ResponseEntity<>(new SystemResponse(1000, "Tìm kiếm popup thành công", popupEntity), HttpStatus.OK);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(new SystemResponse(1001, "Lỗi tìm kiếm popup"), HttpStatus.BAD_REQUEST);
        }
    }
    @PostMapping(value = "/update/{id}")
    public ResponseEntity<?> updatePopup(@PathVariable int id, @ModelAttribute PopupDTO popupDTO) {
        try {
            popupService.updatePopup(id, popupDTO);
            return new ResponseEntity(new SystemResponse(1000, "Successfully uploaded and update popup!"), HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(new SystemResponse(1001, "Error uploaded and update popup not success!"), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping(value = "/remove/{id}")
    public ResponseEntity<?> removePopup(@PathVariable int id) {
        try {
            popupService.remove(id);
            return new ResponseEntity<>(new SystemResponse(1000, "Xóa Popup thành công"), HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(new SystemResponse(1001, "Lỗi Xóa Popup"), HttpStatus.BAD_REQUEST);
        }
    }
}
