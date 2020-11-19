package com.codecool.flight_api_project;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.IOException;

import springfox.documentation.swagger2.annotations.EnableSwagger2;


@EnableSwagger2
@SpringBootApplication
public class FlightApiProjectApplication{

    public static void main(String[] args) throws IOException {
         SpringApplication.run(FlightApiProjectApplication.class, args);
    }
}
