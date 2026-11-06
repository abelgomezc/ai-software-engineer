package com.abel.aisoftwareengineer;

import com.abel.aisoftwareengineer.config.AiseProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties(AiseProperties.class)
public class AiSoftwareEngineerApplication {
    public static void main(String[] args) {
        SpringApplication.run(AiSoftwareEngineerApplication.class, args);
    }
}
