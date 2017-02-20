package com.allegro;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;

import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;

@SpringBootApplication
@ComponentScan(basePackages = "com.allegro")
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Bean
    public ObjectMapper objectMapper(ApplicationContext context){
        return new ObjectMapper();
    }

    @Bean
    public CloseableHttpClient closeableHttpClient(ApplicationContext context){
        return HttpClients.createDefault();
    }

    @Bean
    public DateTimeFormatter dateTimeFormatter(ApplicationContext context){
        return DateTimeFormatter.ofLocalizedDateTime(FormatStyle.FULL);
    }

}
