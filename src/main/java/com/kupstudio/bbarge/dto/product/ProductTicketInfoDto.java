package com.kupstudio.bbarge.dto.product;

import com.kupstudio.bbarge.dto.store.StoreDto;
import com.kupstudio.bbarge.entity.product.ProductTicketEntity;
import com.kupstudio.bbarge.enumClass.product.ChannelEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(description = "티켓 정보 DTO")
public class ProductTicketInfoDto {

    @Schema(description = "티켓 키")
    private String ticketKey;

    @Schema(description = "상품명")
    private String productName;

    @Schema(description = "옵션")
    private String directOption;

    @Schema(description = "티켓상태")
    private boolean isUsed;

    @Schema(description = "매장명")
    private String storeName;

    @Schema(description = "매장 연락처")
    private String storeContactNumber;

    @Schema(description = "매장명")
    private String storeAddress;

    @Schema(description = "구매채널")
    private ChannelEnum channel;

    @Schema(description = "상품판매채널 번호")
    public int getChannelNo() {
        return channel.getIndex();
    }

    @Schema(description = "상품판매채널 이름")
    public String getChannelName() {
        return channel.getMeaning();
    }


    public ProductTicketInfoDto(ProductTicketEntity productTicketEntity, ProductDto productDto, StoreDto storeDto) {
        this.ticketKey = productTicketEntity.getTicketKey();
        this.productName = productDto.getProductName();
        this.directOption = productTicketEntity.getDirectOption();
        this.isUsed = productTicketEntity.isUsed();
        this.storeName = storeDto.getStoreName();
        this.storeContactNumber = storeDto.getStoreContactNumber();
        this.storeAddress = storeDto.getStoreAddress();
        this.channel = productDto.getChannel();
    }

}
