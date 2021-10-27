package com.vsii.enamecard.services;

import com.vsii.enamecard.model.dto.PopupDTO;
import com.vsii.enamecard.model.dto.PostDTO;
import com.vsii.enamecard.model.entities.CategoryEntity;
import com.vsii.enamecard.model.entities.PopupEntity;
import com.vsii.enamecard.model.entities.PostEntity;
import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface PostService {
    PostDTO findById(int id);

    void savePost(String title, String content, MultipartFile file, int channelId, int priority, int creatorId, int modifierId, int category);

    void remove(int id);

    void updatePost(int id, PostDTO postDTO);

    Page<PostDTO.postInfo> getAllPost(int offset, int pageSize);

    List<CategoryEntity> getAllCategory();
}
