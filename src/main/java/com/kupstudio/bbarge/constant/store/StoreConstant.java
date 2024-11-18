package com.kupstudio.bbarge.constant.store;

public class StoreConstant {

    public static String STORE_NAME_IS_EMPTY = "insert StoreName";
    public static String STORE_NAME_NOT_VALID = "StoreName is only English and Korean are available.";

    public static String STORE_CONTACT_NUMBER_NOT_VALID = "storeContactNumber Not valid";
    public static String STORE_CONTACT_NUMBER_IS_EMPTY = "insert ContactNumber";

    public static String CORPORATE_REGISTRATION_NUMBER_NOT_VALID = "taxpayerIdentificationNumber Not valid";
    public static String CORPORATE_REGISTRATION_NUMBER_IS_EMPTY = "insert taxpayerIdentificationNumber or remove it from Request body";

    public static String STORE_NAME_DUPLICATE = "storeName duplicate";

    public static String STORE_IS_NOT_EXIST = "There are no saved STORE";
    public static String STORE_IS_DELETE = "This is a deleted store.";
    public static String STORE_DELETE_IS_NOT_VALID = "매장을 삭제할 수 없습니다. 판매 중인 상품과 담당 운영자를 사제한 후 매장 삭제를 다시 시도해주세요.";

}
