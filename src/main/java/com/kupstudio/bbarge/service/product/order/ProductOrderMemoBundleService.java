package com.kupstudio.bbarge.service.product.order;

import com.kupstudio.bbarge.dto.product.order.ProductOrderMemoDto;
import com.kupstudio.bbarge.exception.common.WriterNotMatchException;
import com.kupstudio.bbarge.requestDto.product.ProductOrderMemoModifyRequestDto;
import com.kupstudio.bbarge.requestDto.product.ProductOrderMemoRequestDto;
import com.kupstudio.bbarge.service.admin.AdminService;
import com.kupstudio.bbarge.utils.EmojiRemoveUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductOrderMemoBundleService {

    private final AdminService adminService;
    private final ProductOrderService productOrderService;
    private final ProductOrderMemoService productOrderMemoService;

    public int insertMemo(int orderNo, ProductOrderMemoRequestDto requestDto) {

        // 유효한 주문번호가 맞는지 확인
        productOrderService.isValidOrderNo(orderNo);

        // 이모지 제거
        requestDto.setMemo(EmojiRemoveUtil.removeEmoji(requestDto.getMemo()));

        // 로그인한 운영자의 adminID
        String adminId = adminService.getAdminUserName();

        ProductOrderMemoDto productOrderMemoDto = new ProductOrderMemoDto().ProductOrderMemoRequestDtoToProductOrderMemoDto(orderNo, requestDto, adminId);

        productOrderMemoService.insertMemo(productOrderMemoDto);

        return productOrderMemoDto.getMemoNo();
    }

    // 특정 주문의 메모 리스트 조회
    public List<ProductOrderMemoDto> getMemoList(int orderNo) {

        // 유효한 주문번호가 맞는지 확인
        productOrderService.isValidOrderNo(orderNo);

        return productOrderMemoService.getMemoList(orderNo);
    }

    public void updateMemo(int orderNo, ProductOrderMemoModifyRequestDto requestDto) {

        int memoNo = requestDto.getMemoNo();
        ProductOrderMemoDto existProductOrderMemoInfo = productOrderMemoService.getMemoDetail(orderNo, memoNo);

        checkWriterValid(existProductOrderMemoInfo);

        // 이모지 제거
        requestDto.setMemo(EmojiRemoveUtil.removeEmoji(requestDto.getMemo()));

        ProductOrderMemoDto productOrderMemoDto = new ProductOrderMemoDto().ProductOrderMemoModifyRequestDtoToProductOrderMemoDto(orderNo, requestDto);

        productOrderMemoService.updateMemo(productOrderMemoDto);
    }

    public void deleteMemo(int orderNo, int memoNo) {

        ProductOrderMemoDto existProductOrderMemoInfo = productOrderMemoService.getMemoDetail(orderNo, memoNo);

        checkWriterValid(existProductOrderMemoInfo);

        productOrderMemoService.deleteMemo(orderNo, memoNo);
    }

    public void checkWriterValid(ProductOrderMemoDto existProductOrderMemoInfo) {

        String adminId = adminService.getAdminUserName();

        // 메모의 작성자와 로그인한 어드민이 같지 않을 경우
        if (!existProductOrderMemoInfo.getAdminID().equals(adminId)) {
            throw new WriterNotMatchException();
        }

    }
}
