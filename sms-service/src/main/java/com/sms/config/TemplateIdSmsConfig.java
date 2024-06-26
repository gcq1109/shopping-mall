package com.sms.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author gcq1109
 * @email gcq1109@126.com
 */
@Component
@Data
@ConfigurationProperties(prefix = "tencent.sms.template-id")
public class TemplateIdSmsConfig {

    private String phoneCode;
    private String sales;
}
