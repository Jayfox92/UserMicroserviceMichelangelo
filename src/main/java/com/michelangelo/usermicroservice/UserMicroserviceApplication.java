package com.michelangelo.usermicroservice;

import com.michelangelo.usermicroservice.exceptions.MediaServiceResponseErrorHandler;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
public class UserMicroserviceApplication {

    public static void main(String[] args) {
        SpringApplication.run(UserMicroserviceApplication.class, args);
    }

    @Bean
    @LoadBalanced
    public RestTemplate restTemplate() {
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.setErrorHandler(new MediaServiceResponseErrorHandler());
        return restTemplate;
    }
}
