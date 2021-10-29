package com.wuyiling.nacosconsumer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.client.discovery.ReactiveDiscoveryClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;


@SpringBootApplication
@EnableDiscoveryClient(autoRegister = false)
public class NacosReactiveConsumer {

    public static void main(String[] args) {
        SpringApplication.run(NacosReactiveConsumer.class, args);
    }

    @RestController
    class HelloController {
        @Autowired
        private ReactiveDiscoveryClient reactiveDiscoveryClient;

        @Autowired
        private DiscoveryClient discoveryClient;

        @Value("nacos-provider")
        private String serviceName;

        @GetMapping("/services")
        public Flux<String> info() {
            return reactiveDiscoveryClient.getServices();
        }

    }

}
