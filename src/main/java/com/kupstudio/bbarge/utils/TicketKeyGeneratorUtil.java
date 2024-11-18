package com.kupstudio.bbarge.utils;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;

public class TicketKeyGeneratorUtil {

    /**
     * 구매수량 10000개 이하만 호출 가능
     */
    public static List<String> generateTicketKey(int quantity) {
        LocalDateTime createAt = LocalDateTime.now();
        Instant instant = createAt.atZone(java.time.ZoneId.systemDefault()).toInstant();
        long timestamp = instant.toEpochMilli();

        // timestamp 마지막 11자리 추출
        String last11Digits = String.valueOf(timestamp).substring(2);

        // 0 ~ 9999 숫자중 랜덤한 숫자를 구매 수량만큼 생성
        Set<Integer> set = new Random().ints(0, 10000)
                .distinct()
                .limit(quantity)
                .boxed()
                .collect(Collectors.toSet());

        List<String> ticketKeyList = new ArrayList<>();

        for (Integer randVal : set) {
            String readValToString = String.format("%04d", randVal);
            // 티켓번호 (15자) = 0000~9999 랜덤 + timeStamp 11자리
            String ticketKey = readValToString + last11Digits;
            ticketKeyList.add(ticketKey);
        }
        return ticketKeyList;
    }
}
