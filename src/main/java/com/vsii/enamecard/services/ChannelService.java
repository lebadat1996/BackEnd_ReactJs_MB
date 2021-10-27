package com.vsii.enamecard.services;
import com.vsii.enamecard.model.entities.ChannelEntity;
import com.vsii.enamecard.model.response.ChannelResponse;
import java.util.List;

public interface ChannelService {
    List<ChannelResponse> getChannelByCurrentAccountContext();

    List<ChannelEntity> getAllChannelEntity();

    ChannelEntity findById(int id);
}
