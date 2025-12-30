package com.vanpine.mcpclient.config;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@Data
@NoArgsConstructor
@ConfigurationProperties(prefix = "spring.ai.doctor")
public class AiDoctorProperties {
    private String systemPrompt;
    private Integer maxMessage;
}
