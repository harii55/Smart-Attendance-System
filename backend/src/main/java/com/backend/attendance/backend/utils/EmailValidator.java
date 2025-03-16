package com.backend.attendance.backend.utils;

import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class EmailValidator {

    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[a-zA-Z]+\\.24bcs(\\d{5})@sst\\.scaler\\.com$");

    public static Optional<String> extractRollNumber(String email){
        Matcher matcher = EMAIL_PATTERN.matcher(email);
        if (matcher.matches()){
            return Optional.of(matcher.group(1));
        }
        return Optional.empty();
    }
}
