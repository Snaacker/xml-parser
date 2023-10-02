package com.snaacker.sample;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class XMLParserApplication {

  public static void main(String[] args) {
    SpringApplication.run(XMLParserApplication.class, args);
  }

}
