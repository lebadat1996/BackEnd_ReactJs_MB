package com.vsii.enamecard.services.impl;

import com.vsii.enamecard.model.dto.PopupDTO;
import com.vsii.enamecard.model.dto.PostDTO;
import com.vsii.enamecard.model.entities.*;
import com.vsii.enamecard.repositories.ChannelRepository;
import com.vsii.enamecard.repositories.PostRepository;
import com.vsii.enamecard.services.CategoryRepository;
import com.vsii.enamecard.services.PostService;
import org.aspectj.util.FileUtil;
import org.modelmapper.ModelMapper;
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
import java.util.stream.Collectors;

@Service
public class PostServiceImpl implements PostService {
    private static final Logger log = LoggerFactory.getLogger(PostServiceImpl.class);
    @Value("${mb.value.path}")
    String uploadBanner;
    @Autowired
    PostRepository postRepository;

    @Autowired
    CategoryRepository categoryRepository;

    @Autowired
    ChannelRepository channelRepository;

    @Override
    public PostDTO findById(int id) {
        log.info("Start Find post by id ");
        try {
            Optional<PostEntity> postEntity = postRepository.findById(id);
            if (postEntity.isPresent() && postEntity.get().getAvatar() != null) {
                log.info("Title: " + postEntity.get().getTitle());
                PostEntity post = postEntity.get();
                byte[] fileContent = FileUtil.readAsByteArray(new File(post.getAvatar()));
                String encodedString = Base64.getEncoder().encodeToString(fileContent);
                post.setAvatar("data:image/png;base64," + encodedString);
                CategoryEntity categoryEntity = categoryRepository.getById(post.getCategoryId());
                PostDTO postDTO = new PostDTO();
                postDTO.setAvatar("data:image/png;base64," + encodedString);
                postDTO.setCategory(categoryEntity.getName());
                postDTO.setContent(post.getContent());
                postDTO.setPriority(post.getPriority());
                postDTO.setChannelId(post.getChannelId());
                postDTO.setStatus(post.getStatus().name());
                postDTO.setTitle(post.getTitle());
                return postDTO;
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.error("Error Find post by id: ");
        }
        return null;
    }

    @Override
    public void savePost(String title, String content, MultipartFile file, int channelId, int priority, int creatorId, int modifierId, int category) {
        Date date = new Date();
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        String strDate = dateFormat.format(date);
        log.info("Create new Post: " + strDate);
        try {
            String fileUpload = uploadBanner;
            String fileName = "";
            if (file != null) {
                fileName = file.getOriginalFilename();
                FileCopyUtils.copy(file.getBytes(), new File(fileUpload + fileName));
            }
            PostEntity postEntity = new PostEntity();
            postEntity.setTitle(title);
            postEntity.setContent(content);
            postEntity.setAvatar(fileUpload + fileName);
            postEntity.setChannelId(channelId);
            postEntity.setPriority(priority);
            postEntity.setCreatorId(creatorId);
            postEntity.setModifierId(modifierId);
            postEntity.setCategoryId(category);
            postEntity.setStatus(PostEntity.Status.ACTIVE);
            postRepository.save(postEntity);
            String endDate = dateFormat.format(date);
            log.info("End create new POST: " + endDate);
        } catch (Exception e) {
            e.printStackTrace();
            String dateError = dateFormat.format(date);
            log.error("Error create new POST: " + dateError);
        }
    }

    @Override
    public void remove(int id) {
        Date date = new Date();
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        String strDate = dateFormat.format(date);
        log.info("Remove Post: " + strDate);
        try {
            Optional<PostEntity> popupEntity = postRepository.findById(id);
            popupEntity.ifPresent(entity -> postRepository.delete(entity));
            String endDate = dateFormat.format(date);
            log.info("End Remove Post: " + endDate);
        } catch (Exception e) {
            e.printStackTrace();
            String dateError = dateFormat.format(date);
            log.error("Error Remove Post: " + dateError);
        }
    }

    @Override
    public void updatePost(int id, PostDTO postDTO) {
        Date date = new Date();
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        String strDate = dateFormat.format(date);
        log.info("Update new post: " + strDate);
        try {
            Optional<PostEntity> postEntity = postRepository.findById(id);
            if (postEntity.isPresent()) {
                PostEntity post = postEntity.get();
                post.setTitle(postDTO.getTitle());
                post.setContent(postDTO.getContent());
                post.setPriority(postDTO.getPriority());
                List<CategoryEntity> categoryEntity = categoryRepository.findAll();
                for (CategoryEntity ca : categoryEntity) {
                    if (postDTO.getCategory().equals(ca.getName())) {
                        post.setCategoryId(ca.getId());
                    }
                }
                String fileName = "";
                if (postDTO.getFile() != null) {
                    fileName = postDTO.getFile().getOriginalFilename();
                    FileCopyUtils.copy(postDTO.getFile().getBytes(), new File(uploadBanner + fileName));
                }
                post.setAvatar(uploadBanner + fileName);
                if (postDTO.getStatus().equals("ACTIVE")) {
                    post.setStatus(PostEntity.Status.ACTIVE);
                    log.info("Status:  " + PostEntity.Status.ACTIVE);
                }
                if (postDTO.getStatus().equals("false")) {
                    log.info("Status:  " + PopupEntity.Status.INACTIVE);
                    post.setStatus(PostEntity.Status.INACTIVE);
                }
                post.setCreatorId(postDTO.getCreatorId());
                post.setModifierId(postDTO.getModifierId());
                postRepository.save(post);
                log.info("End Update new post: " + strDate);
            }
        } catch (Exception e) {
            e.printStackTrace();
            String dateError = dateFormat.format(date);
            log.error("Error update new post: " + dateError);
        }
    }

    @Override
    public Page<PostDTO.postInfo> getAllPost(int offset, int pageSize) {
        Date date = new Date();
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        String strDate = dateFormat.format(date);
        log.info("Find All List Post: " + strDate);

        Page<PostDTO.postInfo> postEntities = null;
        try {
            postEntities = postRepository.listPost(PageRequest.of(offset - 1, pageSize).withSort(Sort.by("id")));
            String endDate = dateFormat.format(date);
            log.info("End Find All List post: " + endDate);
        } catch (Exception e) {
            e.printStackTrace();
            log.error(e.getMessage());
        }
        return postEntities;
    }

    @Override
    public List<CategoryEntity> getAllCategory() {
        return categoryRepository.findAll();
    }
}
