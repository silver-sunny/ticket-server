package com.kupstudio.bbarge.validation.validator.sms;

import java.util.regex.Pattern;

public class PhoneNumberValidator {
    public static boolean isValidPhoneNumber(String phoneNumber) {

        String defaultPhoneNumber = "01000000000";

        if (phoneNumber.equals(defaultPhoneNumber)) {
            return false;
        }

        // 전화번호 정규식
        String phoneNumberRegex = "^\\d{3}\\d{3,4}\\d{4}$";
        return Pattern.matches(phoneNumberRegex, phoneNumber);
    }
}
