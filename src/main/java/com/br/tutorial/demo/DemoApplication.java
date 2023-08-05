package com.br.tutorial.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class DemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);


		// Loop infinito
		while (true) {
			try {
				Thread.sleep(1000); // Pausa de 1 segundo
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

	}
}
