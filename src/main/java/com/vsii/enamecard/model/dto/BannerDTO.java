package com.vsii.enamecard.model.dto;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class BannerDTO {
    String alternativeTitle;
    String destinationUrl;
    MultipartFile file;
    int channelId;
    int priority;
    String status;
    int creatorId;
    int modifierId;
    String avatar;
    String channelName;
    int id;

    public interface infoBanner {
        Integer getId();

        String getAlternativeTitle();

        String getDestinationUrl();

        String getAvatar();

        String getStatus();

        String getPriority();

        String getChannelName();
    }
}
