package com.codecool.flightapi;

import java.io.IOException;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@SpringBootApplication
@ConfigurationPropertiesScan
public class FlightApiApplication {

    public static void main(String[] args) throws IOException {
        SpringApplication.run(FlightApiApplication.class, args);
    }
}
