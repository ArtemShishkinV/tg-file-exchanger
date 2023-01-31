package com.shishkin.config;

import com.shishkin.CryptoTool;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class NodeConfiguration {
    @Value("${upload.static.salt}")
    private String salt;

    @Bean
    public CryptoTool cryptoTool() {
        return new CryptoTool(salt);
    }
}
