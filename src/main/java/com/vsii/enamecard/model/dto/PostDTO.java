package com.vsii.enamecard.model.dto;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class PostDTO {
    public String title;
    public String content;
    public MultipartFile file;
    public String avatar;
    public int channelId;
    public String channel;
    public int priority;
    public String status;
    public int categoryId;
    public String category;
    public int creatorId;
    public int modifierId;
    public String nameChannel;
    public String nameCategory;


    public interface postInfo {
        Integer getId();

        String getTitle();

        String getContent();

        String getAvatar();

        String getNameChannel();

        String getPriority();

        String getNameCategory();

        String getStatus();
    }
}
