package com.kupstudio.bbarge.dto.product;

import com.kupstudio.bbarge.enumClass.product.ChannelEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChannelDetailDto {

    @Schema(description = "상품판매채널")
    private ChannelEnum channelEnum;
    @Schema(description = "상품판매채널 이름")
    private String name;
    @Schema(description = "디테일 URL")
    private String detailUrl;
    @Schema(description = "아이콘")
    private String icon;

    public ChannelDetailDto() {
        this.channelEnum = channelEnum;
        this.name = name;
        this.detailUrl = detailUrl;
        this.icon = icon;
    }

    public ChannelDetailDto(ChannelEnum channelEnum, String name, String detailUrl) {
        this.channelEnum = channelEnum;
        this.name = name;
        this.detailUrl = detailUrl;
    }

}
