package com.kupstudio.bbarge.exceptionService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class ApiResponseService {

    /**
     * response가 없을때 사용
     *
     *
     * @return
     */
    public static ResponseEntity toResponseEntity() {

        return ResponseEntity.status(HttpStatus.OK).build();

    }

    //response error code가 있어야 될때

    /**
     * response code custom이 존재할때
     *
     * @param responseCode
     * @param message
     * @return
     * @param <T>
     */
    public static <T> ResponseEntity toResponseEntity(int responseCode,String message) {
        return ResponseEntity
                .status(responseCode)
                .body(message);

    }

    /**
     * response data가 존재할때
     * response status 200
     *
     * @param data
     * @return
     * @param <T>
     */
    public static <T> ResponseEntity toResponseEntity(T data) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(data);
    }

//    //response header가 있을 때 ..
//    public static <T> ResponseEntity<ResponseData<T>> toResponseEntity(ResponseCode responseCode,
//                                                                       MultiValueMap<String, String> header, T data) {
//        return ResponseEntity
//                .status(responseCode.getHttpStatus())
//                .header(String.valueOf(header))
//                .body(ResponseData.<T>builder()
//                        .code(responseCode)
//                        .data(data)
//                        .build()
//                );
//    }


}
