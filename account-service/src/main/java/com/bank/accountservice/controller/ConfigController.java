package com.bank.accountservice.controller;

import com.bank.accountservice.config.BankProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/config")
@RequiredArgsConstructor
public class ConfigController {

    private final BankProperties bankProperties;
    private final Environment environment;

    @GetMapping
    public Map<String, Object> getConfig() {
        Map<String, Object> config = new HashMap<>();
        config.put("bankName", bankProperties.getName());
        config.put("maxDailyTransfer", bankProperties.getMaxDailyTransfer());
        config.put("supportEmail", bankProperties.getSupportEmail());
        config.put("features", bankProperties.getFeatures());
        config.put("api", bankProperties.getApi());
        config.put("activeProfiles", environment.getActiveProfiles());
        config.put("serverPort", environment.getProperty("server.port"));
        return config;
    }

    @GetMapping("/health")
    public Map<String, String> health() {
        Map<String, String> status = new HashMap<>();
        status.put("status", "UP");
        status.put("service", "account-service");
        status.put("profile", environment.getActiveProfiles().length > 0
                ? environment.getActiveProfiles()[0]
                : "default");
        return status;
    }
}
