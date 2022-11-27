package com.dmitry.muravev.chat.masking;

public interface MaskingUtil {

    String maskLine(String line);

    String maskHeader(String headerName, String headerValue);
}
