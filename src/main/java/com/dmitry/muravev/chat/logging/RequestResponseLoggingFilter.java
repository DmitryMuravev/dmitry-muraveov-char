package com.dmitry.muravev.chat.logging;

import com.dmitry.muravev.chat.masking.MaskingUtil;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Collections;
import java.util.stream.Stream;

@RequiredArgsConstructor
public class RequestResponseLoggingFilter extends OncePerRequestFilter {

    private final static Logger log = LoggerFactory.getLogger(RequestResponseLoggingFilter.class);

    private final MaskingUtil maskingUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        doFilterWrapped(new ContentCachingRequestWrapper(request),
                new ContentCachingResponseWrapper(response), filterChain);
    }

    /**
     * Process wrapped request and response.
     *
     * @param request {@link ContentCachingRequestWrapper} wrapped request.
     * @param response {@link ContentCachingResponseWrapper} wrapped response.
     */
    protected void doFilterWrapped(ContentCachingRequestWrapper request,
                                   ContentCachingResponseWrapper response,
                                   FilterChain filterChain) throws ServletException, IOException {

        filterChain.doFilter(request, response);

        logRequest(request);
        logResponse(response);

        response.copyBodyToResponse();
    }

    /**
     * Log request data.
     *
     * @param request {@link ContentCachingRequestWrapper} object which data
     *                                                    must be logged.
     */
    private void logRequest(ContentCachingRequestWrapper request) {
        StringBuilder messageBuilder = new StringBuilder();

        messageBuilder.append(System.lineSeparator()).append("REQUEST:")
                .append(System.lineSeparator());

        logRequestUrl(request, messageBuilder);
        logRequestHeader(request, messageBuilder);
        logRequestBody(request, messageBuilder);

        log.info(messageBuilder.toString());
    }

    /**
     * Combine request URL with method and query string and append it to provided {@link StringBuilder}.
     *
     * @param request {@link ContentCachingRequestWrapper} object from which URL data is retrieved.
     * @param messageBuilder {@link StringBuilder} that collect URL data for logging.
     */
    private void logRequestUrl(ContentCachingRequestWrapper request, StringBuilder messageBuilder) {
        messageBuilder.append(request.getMethod()).append("/ ")
                .append(request.getRequestURL());

        if (StringUtils.hasText(request.getQueryString())) {
            messageBuilder.append("?").append(request.getQueryString());
        }
        messageBuilder.append(System.lineSeparator());
    }

    /**
     * Combine request headers data, mask and append them to provided {@link StringBuilder}.
     *
     * @param request {@link ContentCachingRequestWrapper} object from which header data is retrieved.
     * @param messageBuilder {@link StringBuilder} that collect headers data for logging.
     */
    private void logRequestHeader(ContentCachingRequestWrapper request, StringBuilder messageBuilder) {
        Collections.list(request.getHeaderNames())
                .forEach(headerName -> Collections.list(request.getHeaders(headerName))
                        .forEach(headerValue -> messageBuilder
                                .append(maskingUtil.maskHeader(headerName, headerValue))
                                .append(System.lineSeparator())));
        messageBuilder.append(System.lineSeparator());
    }

    /**
     * Append request body content to provided {@link StringBuilder}.
     *
     * @param request {@link ContentCachingRequestWrapper} object from which body data is retrieved.
     * @param messageBuilder {@link StringBuilder} that collect headers data for logging.
     */
    private void logRequestBody(ContentCachingRequestWrapper request, StringBuilder messageBuilder) {
        byte[] content = request.getContentAsByteArray();
        if (content.length > 0) {
            logContent(content, request.getCharacterEncoding(), messageBuilder);
        }
    }

    /**
     * Log response data.
     *
     * @param response {@link ContentCachingResponseWrapper} object which data
     *                                                      must be logged.
     */
    private void logResponse(ContentCachingResponseWrapper response) {
        StringBuilder messageBuilder = new StringBuilder();

        messageBuilder.append(System.lineSeparator()).append("RESPONSE:")
                .append(System.lineSeparator());

        logResponseStatus(response, messageBuilder);
        logResponseHeaders(response, messageBuilder);
        logResponseBody(response, messageBuilder);

        log.info(messageBuilder.toString());
    }

    /**
     * Combine response status data and append it to provided {@link StringBuilder}.
     *
     * @param response {@link ContentCachingRequestWrapper} object from which status data is retrieved.
     * @param messageBuilder {@link StringBuilder} that collect headers data for logging.
     */
    private void logResponseStatus(ContentCachingResponseWrapper response, StringBuilder messageBuilder) {
        int status = response.getStatus();
        messageBuilder.append(String.format("%s %s", status, HttpStatus.valueOf(status)
                .getReasonPhrase())).append(System.lineSeparator());
    }

    /**
     * Combine response headers data, mask and append them to provided {@link StringBuilder}.
     *
     * @param response {@link ContentCachingResponseWrapper} object from which header data is retrieved.
     * @param messageBuilder {@link StringBuilder} that collect headers data for logging.
     */
    private void logResponseHeaders(ContentCachingResponseWrapper response, StringBuilder messageBuilder) {
        response.getHeaderNames()
                .forEach(headerName -> response.getHeaders(headerName)
                        .forEach(headerValue -> messageBuilder
                                .append(maskingUtil.maskHeader(headerName, headerValue))));
        messageBuilder.append(System.lineSeparator());
    }

    /**
     * Append response body content to provided {@link StringBuilder}.
     *
     * @param response {@link ContentCachingRequestWrapper} object from which body data is retrieved.
     * @param messageBuilder {@link StringBuilder} that collect headers data for logging.
     */
    private void logResponseBody(ContentCachingResponseWrapper response, StringBuilder messageBuilder) {
        byte[] content = response.getContentAsByteArray();
        if (content.length > 0) {
            logContent(content, response.getCharacterEncoding(), messageBuilder);
        }
    }

    /**
     * Process byte content, mask sensitive data and append it to provided {@link StringBuilder}.
     *
     * @param content byte array represented logging content.
     * @param contentEncoding content encoding.
     * @param messageBuilder {@link StringBuilder} that collect content data for logging.
     */
    private void logContent(byte[] content, String contentEncoding, StringBuilder messageBuilder) {
        try {
            String contentString = new String(content, contentEncoding);
            Stream.of(contentString.split(System.lineSeparator()))
                    .map(maskingUtil::maskLine)
                    .forEach(line -> messageBuilder
                            .append(" ").append(line).append(System.lineSeparator()));
        } catch (UnsupportedEncodingException e) {
            messageBuilder
                    .append(String.format("[%d bytes content]", content.length))
                    .append(System.lineSeparator());
        }

    }
}