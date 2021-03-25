package com.dentalclinic;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class DesafioJavaApplication {

	public static void main(String[] args) {
		SpringApplication.run(DesafioJavaApplication.class, args);
	}
	
	/*@Bean
    public CommandLineRunner run(Sistema sistema) throws Exception {
        return args -> {
           sistema.incluirUsuario();
        };
    }*/
}
