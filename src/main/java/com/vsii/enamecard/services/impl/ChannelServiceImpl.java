package com.vsii.enamecard.services.impl;

import com.vsii.enamecard.exceptions.HttpErrorException;
import com.vsii.enamecard.model.dto.AccountDTO;
import com.vsii.enamecard.model.entities.ChannelEntity;
import com.vsii.enamecard.model.response.ChannelResponse;
import com.vsii.enamecard.repositories.ChannelRepository;
import com.vsii.enamecard.services.AccountService;
import com.vsii.enamecard.services.ChannelService;
import com.vsii.enamecard.utils.Constant;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ChannelServiceImpl implements ChannelService {


    private final ChannelRepository repository;
    private final ModelMapper modelMapper;
    private final AccountService accountService;

    public ChannelServiceImpl(ChannelRepository repository, ModelMapper modelMapper, AccountService accountService) {
        this.repository = repository;
        this.modelMapper = modelMapper;
        this.accountService = accountService;
    }

    public List<ChannelResponse> getChannelByCurrentAccountContext(){
        AccountDTO currentAccountContext = accountService.getCurrentAccountContext();
        if (currentAccountContext.getRoleName().equals(Constant.NAME_ROLE_ADMIN)){
            return repository.findAll().stream().map(channelEntity -> modelMapper.map(channelEntity,ChannelResponse.class)).collect(Collectors.toList());
        }
        List<ChannelResponse> channelResponses = new ArrayList<>();
        ChannelEntity channelEntity = repository.findById(currentAccountContext.getChannelId()).orElseThrow(() -> HttpErrorException.badRequest("channel is not existed"));
        channelResponses.add(modelMapper.map(channelEntity,ChannelResponse.class));
        return channelResponses;
    }

    @Override
    public List<ChannelEntity> getAllChannelEntity() {
        return repository.findAll();
    }

    @Override
    public ChannelEntity findById(int id) {
        return repository.getById(id);
    }

}
