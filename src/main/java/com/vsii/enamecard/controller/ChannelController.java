package com.vsii.enamecard.controller;

import com.vsii.enamecard.model.entities.ChannelEntity;
import com.vsii.enamecard.model.response.ChannelResponse;
import com.vsii.enamecard.services.ChannelService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/channel")
@CrossOrigin("*")
public class ChannelController {

    private final ChannelService service;


    public ChannelController(ChannelService service) {
        this.service = service;
    }

    @GetMapping()
    ResponseEntity<List<ChannelResponse>> getChannelByCurrentAccountContext() {
        return ResponseEntity.ok(service.getChannelByCurrentAccountContext());
    }

    @GetMapping(value = "/getAll")
    public ResponseEntity<?> getAllChannel() {
        try{
            List<ChannelEntity> channelEntityList = service.getAllChannelEntity();
            return new ResponseEntity<>(channelEntityList, HttpStatus.OK);
        }catch (Exception e){
            e.printStackTrace();
            return new ResponseEntity<>("Lỗi tìm kiếm channel", HttpStatus.OK);
        }
    }
}
