package com.jstronkhorst.springit;

import org.ocpsoft.prettytime.PrettyTime;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableTransactionManagement
public class SpringitApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringitApplication.class, args);
    }

    @Bean
    PrettyTime prettyTime() {
        return new PrettyTime();
    }
}
