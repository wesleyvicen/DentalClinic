package com.sysmei;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class SysmeiJavaApplication {

  public static void main(String[] args) {
    SpringApplication.run(SysmeiJavaApplication.class, args);
  }

}
