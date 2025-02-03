package ru.job4j.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class AppConfig {

    @Value("${telegram.bot.name}")
    private String appName;

    @Value("${app.version}")
    private String appVersion;

    @Value("${app.url}")
    private String appUrl;

    @Value("${app.timeout}")
    private int timeout;

    public void printConfig() {
        System.out.println("App Name: " + appName);
    }
}