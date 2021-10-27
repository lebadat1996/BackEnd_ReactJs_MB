package com.vsii.enamecard.model.dto;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class PopupDTO {
    private String alternativeTitle;
    private String destinationUrl;
    private MultipartFile file;
    private int channelId;
    private int priority;
    private String status;
    private int creatorId;
    private int modifierId;
}
