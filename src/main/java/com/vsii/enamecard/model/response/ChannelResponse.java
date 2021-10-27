package com.vsii.enamecard.model.response;

import lombok.*;


@NoArgsConstructor
@AllArgsConstructor
@Getter
public class ChannelResponse {

    private Integer id;

    private String name;

    public ChannelResponse setId(Integer id) {
        this.id = id;
        return this;
    }

    public ChannelResponse setName(String name) {
        this.name = name;
        return this;
    }
}
