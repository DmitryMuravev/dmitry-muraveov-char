package com.dmitry.muravev.chat.config;

import com.dmitry.muravev.chat.logging.RequestResponseLoggingFilter;
import com.dmitry.muravev.chat.masking.MaskingInfo;
import com.dmitry.muravev.chat.masking.MaskingUtil;
import com.dmitry.muravev.chat.masking.MaskingUtilImpl;
import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;

@Configuration
@ConfigurationProperties(prefix = "masking")
public class LoggingConfig {

    @Getter
    private final List<MaskingInfo> bodyPatterns = new ArrayList<>();

    @Getter
    private final List<String> sensitiveHeaders = new ArrayList<>();

    @Bean
    public FilterRegistrationBean<RequestResponseLoggingFilter> logFilter(RequestResponseLoggingFilter filter) {
        FilterRegistrationBean<RequestResponseLoggingFilter> registrationBean
                = new FilterRegistrationBean<>();

        registrationBean.setFilter(filter);
        registrationBean.addUrlPatterns("/*");
        registrationBean.setOrder(2);

        return registrationBean;

    }

    @Bean
    public RequestResponseLoggingFilter requestResponseFilter() {

        MaskingUtil maskingUtil = new MaskingUtilImpl(bodyPatterns, sensitiveHeaders);
        return new RequestResponseLoggingFilter(maskingUtil);
    }

}