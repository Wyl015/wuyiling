package com.wuyiling.compositprovider;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.client.serviceregistry.AutoServiceRegistrationProperties;

@SpringBootApplication
@EnableConfigurationProperties()//使AutoServiceRegistrationProperties生效
public class CompositProviderApplication {

    public static void main(String[] args) {
        SpringApplication.run(CompositProviderApplication.class, args);
    }

}
