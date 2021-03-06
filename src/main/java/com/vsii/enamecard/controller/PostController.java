package com.vsii.enamecard.controller;


import com.vsii.enamecard.model.dto.PostDTO;
import com.vsii.enamecard.model.entities.CategoryEntity;

import com.vsii.enamecard.model.response.SystemResponse;
import com.vsii.enamecard.services.PostService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin("*")
@RequestMapping(value = "/api/post")
public class PostController {
    @Autowired
    PostService postService;

    @PostMapping(value = "/save")
    public ResponseEntity<?> savePopup(@ModelAttribute PostDTO postDTO) {
        try {
            postService.savePost(postDTO.getTitle(), postDTO.getContent(), postDTO.getFile(), postDTO.getChannelId(), postDTO.getPriority(), postDTO.getCreatorId(), postDTO.getModifierId(), postDTO.getCategoryId());
            return new ResponseEntity(new SystemResponse(1000, "Successfully uploaded and save Post!"), HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(new SystemResponse(1001, "Error uploaded and create Post not success!"), HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping(value = "/update/{id}")
    public ResponseEntity<?> updatePopup(@PathVariable int id, @ModelAttribute PostDTO postDTO) {
        try {
            postService.updatePost(id, postDTO);
            return new ResponseEntity(new SystemResponse(1000, "Successfully uploaded and save Post!"), HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(new SystemResponse(1001, "Error uploaded and create Post not success!"), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping(value = "/getAllCategory")
    public ResponseEntity<?> getAllCategory() {
        try {
            List<CategoryEntity> categoryEntityList = postService.getAllCategory();
            return new ResponseEntity<>(categoryEntityList, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>("L???i t??m ki???m category", HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping(value = "/view")
    public ResponseEntity<Page<PostDTO.postInfo>> postList(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "5") int size) {
        try {
            return new ResponseEntity<>(postService.getAllPost(page, size), HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping(value = "/getPostById/{id}")
    public ResponseEntity<?> getPostById(@PathVariable int id) {
        try {
            PostDTO postEntity = postService.findById(id);
            if (postEntity == null) {
                return new ResponseEntity<>(new SystemResponse(1000, "Kh??ng t??m th???y post"), HttpStatus.OK);
            } else {
                return new ResponseEntity<>(new SystemResponse(1000, "T??m ki???m post th??nh c??ng", postEntity), HttpStatus.OK);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(new SystemResponse(1001, "L???i t??m ki???m post"), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping(value = "/remove/{id}")
    public ResponseEntity<?> removePost(@PathVariable int id) {
        try {
            postService.remove(id);
            return new ResponseEntity<>(new SystemResponse(1000, "X??a Postth??nh c??ng"), HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(new SystemResponse(1001, "L???i X??a Post"), HttpStatus.BAD_REQUEST);
        }
    }
}
