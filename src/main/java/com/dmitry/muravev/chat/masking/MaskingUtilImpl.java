package com.dmitry.muravev.chat.masking;

import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.regex.Matcher;

@RequiredArgsConstructor
public class MaskingUtilImpl implements MaskingUtil{

    private static final String SENSITIVE_HEADER_REPLACEMENT = "*******";
    private static final String HEADER_FORMAT = "%s: %s";

    private final List<MaskingInfo> bodyMaskingInfoList;
    private final List<String> sensitiveHeaders;

    public String maskLine(String line){

        String resultLine = line;

        for (MaskingInfo mi : bodyMaskingInfoList) {
            Matcher currentLineMatcher = mi.getPattern().matcher(resultLine);
            resultLine = currentLineMatcher.replaceAll(mi.getReplace());
        }

        return resultLine;
    }

    public String maskHeader(String headerName, String headerValue) {
        String resultHeader;

        if (isSensitiveHeader(headerName)) {
            resultHeader = String.format(HEADER_FORMAT, headerName, SENSITIVE_HEADER_REPLACEMENT);
        } else {
            resultHeader = String.format(HEADER_FORMAT, headerName, headerValue);
        }

        return resultHeader;
    }

    private boolean isSensitiveHeader(String headerName) {
        return sensitiveHeaders.contains(headerName.toLowerCase());
    }
}
