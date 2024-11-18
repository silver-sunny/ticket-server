package com.kupstudio.bbarge.dto.store;

import com.kupstudio.bbarge.requestDto.store.StoreModifyRequestDto;
import com.kupstudio.bbarge.requestDto.store.StoreRequestDto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class StoreDto {

    @Schema(description = "매장 번호")
    private int storeNo;

    @Schema(description = "매장 이름")
    private String storeName;

    @Schema(description = "매장 연락처")
    private String storeContactNumber;

    @Schema(description = "매장 주소")
    private String storeAddress;

    @Schema(description = "매장 이름")
    private String companyName;

    @Schema(description = "매장 사업자 등록 번호")
    private String taxpayerIdentificationNumber;

    @Schema(description = "매장 정보 등록 시간")
    private LocalDateTime createAt;

    @Schema(description = "매장 정보 수정 시간")
    private LocalDateTime modifyAt;

    @Schema(description = "삭제 여부")
    private boolean isDelete;

    public StoreDto() {
        this.storeNo = 0;
        this.storeName = "";
        this.storeContactNumber = "";
        this.storeAddress = "";
        this.companyName = "";
        this.taxpayerIdentificationNumber = "";
        this.createAt = null;
        this.modifyAt = null;
        this.isDelete = false;
    }

    public StoreDto StoreRequestDtoToStoreDto(StoreRequestDto requestDto, String storeAddress, String companyName) {
        LocalDateTime currentDate = LocalDateTime.now().withNano(0);

        return StoreDto.builder()
                .storeName(requestDto.getStoreName())
                .storeContactNumber(requestDto.getStoreContactNumber())
                .storeAddress(storeAddress)
                .companyName(companyName)
                .taxpayerIdentificationNumber(requestDto.getTaxpayerIdentificationNumber())
                .createAt(currentDate)
                .build();
    }

    public StoreDto StoreModifyRequestDtoToStoreDto(StoreModifyRequestDto requestDto, int storeNo, String storeAddress, String companyName) {
        LocalDateTime currentDate = LocalDateTime.now().withNano(0);

        return StoreDto.builder()
                .storeNo(storeNo)
                .storeContactNumber(requestDto.getStoreContactNumber())
                .storeAddress(storeAddress)
                .companyName(companyName)
                .taxpayerIdentificationNumber(requestDto.getTaxpayerIdentificationNumber())
                .modifyAt(currentDate)
                .build();
    }
}

