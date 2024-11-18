package com.kupstudio.bbarge.service.product;

import com.kupstudio.bbarge.constant.product.ProductTicketConstant;
import com.kupstudio.bbarge.dao.dbProduct.product.ProductTicketDao;
import com.kupstudio.bbarge.dto.pagination.PageHelperDto;
import com.kupstudio.bbarge.dto.pagination.PagingResponse;
import com.kupstudio.bbarge.dto.product.ProductDto;
import com.kupstudio.bbarge.dto.product.ProductTicketListDto;
import com.kupstudio.bbarge.dto.product.order.OrderDetailDto;
import com.kupstudio.bbarge.entity.product.ProductTicketEntity;
import com.kupstudio.bbarge.enumClass.product.DetailedConditionEnum;
import com.kupstudio.bbarge.exception.common.PostNotExistException;
import com.kupstudio.bbarge.service.product.order.ProductOrderService;
import com.kupstudio.bbarge.service.store.StoreService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductTicketService {

    private final ProductTicketDao productTicketDao;
    private final ProductService productService;
    private final ProductOrderService productOrderService;
    private final StoreService storeService;

    public ProductTicketEntity getProductTicketByTicketKey(String ticketKey) {
        return productTicketDao.getProductTicketByTicketKey(ticketKey);
    }

    public ProductTicketEntity getProductTicketByTicketKeyAndExistCheck(String ticketKey) {
        ProductTicketEntity productTicketEntity = getProductTicketByTicketKey(ticketKey);
        if (ObjectUtils.isEmpty(productTicketEntity)) {
            throw new PostNotExistException(ProductTicketConstant.NOT_EXIST_TICKET);
        }
        return productTicketEntity;
    }


    public void insertProductTicket(ProductTicketEntity productTicketEntity) {
        productTicketDao.insertTicket(productTicketEntity);
    }

    public void insertProductTicketList(List<ProductTicketEntity> productTicketEntityList) {
        productTicketDao.insertTicketList(productTicketEntityList);
    }

    public void updateProductTicketUsed(List<String> ticketKeys, int adminNo) {
        productTicketDao.updateProductTicketUsed(ticketKeys, adminNo);
    }

    public PagingResponse getProductTicketList(DetailedConditionEnum detailedConditionEnum, String searchWord, int countPerPage, int page) {

        int total = productTicketDao.getTotal(detailedConditionEnum, searchWord);

        PageHelperDto params = new PageHelperDto(countPerPage, page, total, null);

        if (total == 0) return new PagingResponse(new ArrayList(), params);

        int offSet = (page - 1) * countPerPage;

        List<ProductTicketListDto> productTicketList = new ArrayList<>();

        List<ProductTicketEntity> list = productTicketDao.getProductTicketList(detailedConditionEnum, searchWord, countPerPage, offSet);
        for (ProductTicketEntity entity : list) {
            productTicketList.add(getProductTicketListDto(entity, entity.getOrderNo()));
        }

        return new PagingResponse(productTicketList, params);
    }

    public PagingResponse getProductTicketListByOrderNo(String searchWord, int countPerPage, int page) {

        PageHelperDto params;

        // 입력된 키 값과 일치하는 티켓이 없을 경우 빈 페이지 셋팅해서 return
        ProductTicketEntity productTicketEntity = getProductTicketByTicketKey(searchWord);
        if (ObjectUtils.isEmpty(productTicketEntity)) {
            params = new PageHelperDto(countPerPage, page, 0, null);
            return new PagingResponse(new ArrayList(), params);
        }

        int orderNo = productTicketEntity.getOrderNo();

        int total;
        List<ProductTicketListDto> productTicketList = new ArrayList<>();

        // 1페이지의 경우 검색한 티켓번호가 가장 위로 나오고 같은 주문번호의 다른 티켓은 티켓번호 내림차순 정렬
        if (page == 1) {
            total = productTicketDao.getTotalByTicketKeyAndOrderNo(orderNo);
            int offSet = 0;

            productTicketList.add(getProductTicketListDto(productTicketEntity, orderNo));

            List<ProductTicketEntity> list = productTicketDao.getProductTicketListByTicketKeyAndOrderNo(productTicketEntity.getTicketKey(), orderNo, countPerPage - 1, offSet);
            for (ProductTicketEntity entity : list) {
                productTicketList.add(getProductTicketListDto(entity, orderNo));
            }
        } else {
            // 1페이지가 아닐 경우 같은 주문번호의 티켓을 티켓번호 기준 내림차순 정렬
            total = productTicketDao.getTotalByTicketKeyAndOrderNo(orderNo);

            int offSet = (page - 1) * countPerPage - 1;

            List<ProductTicketEntity> list = productTicketDao.getProductTicketListByTicketKeyAndOrderNo(productTicketEntity.getTicketKey(), orderNo, countPerPage, offSet);
            for (ProductTicketEntity entity : list) {
                productTicketList.add(getProductTicketListDto(entity, orderNo));
            }
        }

        params = new PageHelperDto(countPerPage, page, total, null);

        return new PagingResponse(productTicketList, params);
    }

    public ProductTicketListDto getProductTicketListDto(ProductTicketEntity productTicketEntity, int orderNo) {
        ProductDto productDto = productService.getProductDetail(productTicketEntity.getProductNo());
        OrderDetailDto orderDetailDto = productOrderService.getOrderDetail(orderNo);
        String storeName = storeService.getStoreName(productTicketEntity.getStoreNo());
        return new ProductTicketListDto(productTicketEntity, productDto, orderDetailDto, storeName);
    }

    /**
     * orderNo 에 해당하는 모든 티켓이 사용되었는지 여부 반환
     *
     * @param orderNo
     * @return
     */
    public boolean isAllTicketByOrderUsed(int orderNo) {
        return productTicketDao.isAllTicketByOrderUsed(orderNo);
    }

    public String getChannelOrderId(String ticketKey) {
        return productTicketDao.getChannelOrderId(ticketKey);
    }

    public void deleteProductTicketByOrder(int orderNo) {
        productTicketDao.deleteProductTicketByOrder(orderNo);
    }

    public List<ProductTicketEntity> getProductTicketListByOrderNo(int orderNo) {
        return productTicketDao.getProductTicketListByOrderNo(orderNo);
    }

    public int countProductTicketUsedByOrderNo(int orderNo,
                                               boolean isUsed) {
        return productTicketDao.countProductTicketUsedByOrderNo(orderNo, isUsed);
    }

    public boolean isProductTicketUsed(int orderNo) {
        return this.countProductTicketUsedByOrderNo(orderNo, true) > 0;
    }
}
