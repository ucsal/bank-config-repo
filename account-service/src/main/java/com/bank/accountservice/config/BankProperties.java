package com.bank.accountservice.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Component;

import java.util.Map;

@Data
@Component
@RefreshScope
@ConfigurationProperties(prefix = "bank")
public class BankProperties {
    
    private String name;
    private Double maxDailyTransfer;
    private String supportEmail;
    private Map<String, Boolean> features;
    private ApiConfig api;
    
    @Data
    public static class ApiConfig {
        private String externalValidationUrl;
        private Integer timeout;
        private String apiKey;
    }
}