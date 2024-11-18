package com.kupstudio.bbarge.requestDto.store;

import com.kupstudio.bbarge.validation.validInterface.store.ContactNumberValid;
import com.kupstudio.bbarge.validation.validInterface.store.StoreNameValid;
import com.kupstudio.bbarge.validation.validInterface.store.TaxpayerIdentificationNumberValid;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Size;

@Getter
@Setter
public class StoreRequestDto {

    @Schema(description = "매장명", example = "이름")
    @Size(min = 1, max = 100, message = "The character limit for the 'storeName' is 1 to 14.")
    @StoreNameValid
    private String storeName;

    @Schema(description = "매장 연락처", example = "010-0000-0000")
    @Size(min = 1, max = 20, message = "The character limit for the 'storeContactNumber' is 1 to 20.")
    @ContactNumberValid
    private String storeContactNumber;

    @Schema(description = "매장 주소", example = "주소")
    @Size(min = 1, max = 100, message = "The character limit for the 'storeAddress' is 1 to 100.")
    private String storeAddress;

    @Schema(description = "법인명", example = " ")
    @Size(max = 100, message = "companyName max is 100")
    private String companyName;

    @Schema(description = "사업자 등록 번호", example = " ")
    @Size(max = 12, message = "taxpayerIdentificationNumber max is 12")
    @TaxpayerIdentificationNumberValid
    private String taxpayerIdentificationNumber;
}
