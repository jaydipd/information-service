package com.informationservice;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class InformationServiceApplication {



    public static void main(String[] args) {
        SpringApplication.run(InformationServiceApplication.class, args);
    }

    /*
    open:
  api:
    id: 67162a3ba340d7cf19445ecc45b5a25a
    uri: http://api.openweathermap.org/data/2.5/weather
     */
}
