package com.kupstudio.bbarge.utils;


import org.apache.commons.lang3.StringUtils;

public class ValidationUtil {

    public static boolean adminIdValidate(String adminId) {
        return !StringUtils.isEmpty(adminId) && adminId.length() <= 14 && adminId.matches("[a-zA-Z0-9]+");
    }

    public static boolean adminPasswordValidate(String password) {
        return isLengthValid(password) && containsValidCharacters(password);
    }

    private static boolean isLengthValid(String password) {
        int minLength = 8;
        int maxLength = 14;
        int length = password.length();
        return length >= minLength && length <= maxLength;
    }

    private static boolean containsValidCharacters(String password) {
        String pattern = "^[a-zA-Z0-9]+$";
        return password.matches(pattern);
    }


}
