package com.salaboy.customercatalog;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.testcontainers.context.ImportTestcontainers;

import io.diagrid.dapr.profiles.DaprBasicProfile;


@SpringBootApplication
public class CustomerCatalogTestApp {
    public static void main(String[] args) {
        SpringApplication.from(CustomerCatalogApplication::main)
        .with(TestConfigurations.class)
         .run(args);
    }

   @ImportTestcontainers(DaprBasicProfile.class) 
   static class TestConfigurations{

   }

}
