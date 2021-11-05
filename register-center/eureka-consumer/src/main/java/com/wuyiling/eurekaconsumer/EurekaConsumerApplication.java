package com.wuyiling.eurekaconsumer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@SpringBootApplication
@EnableDiscoveryClient(autoRegister = false)
public class EurekaConsumerApplication {

    public static void main(String[] args) {
        SpringApplication.run(EurekaConsumerApplication.class, args);
    }

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    @RestController
    class HelloCOntroller {
        @Autowired
        private DiscoveryClient discoveryClient;

        @Autowired
        private RestTemplate restTemplate;

        @Value("eureka-provider")
        private String serviceName;

        @GetMapping("/info")
        public String info(){
            List<ServiceInstance> serviceInstances = discoveryClient.getInstances(serviceName);
            StringBuilder sb = new StringBuilder();
            sb.append("All services:" + discoveryClient.getServices() + "<br/>");
            serviceInstances.forEach( instance -> {
                sb.append("[serviceId: " + instance.getServiceId() +
                        ",host: " + instance.getHost() +
                        ",port: " + instance.getPort() +
                        " ]"
                );
                sb.append("<br/>");
            });
            return sb.toString();
        }

        @GetMapping("/hello")
        public String hello() {
            List<ServiceInstance> serviceInstances = discoveryClient.getInstances(serviceName);
            ServiceInstance serviceInstance = serviceInstances.stream().findAny()
                    .orElseThrow(() -> new IllegalStateException("no" + serviceName + "instance avaliable"));
            return restTemplate.getForObject(
                    "http://" + serviceInstance.getHost() + ":" + serviceInstance.getPort() + "/echo?name=eureka"
                    , String.class
            );
        }
    }
}
