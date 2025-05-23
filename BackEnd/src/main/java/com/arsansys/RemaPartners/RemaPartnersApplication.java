package com.arsansys.RemaPartners;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * Clase principal para iniciar la aplicación Spring Boot de Rema Partners.
 * 
 * Esta clase contiene el método main que arranca la aplicación y habilita la
 * programación de tareas mediante la anotación {@link EnableScheduling}.
 */
@SpringBootApplication
@EnableScheduling
public class RemaPartnersApplication {

	/**
	 * Método principal que inicia la aplicación Spring Boot.
	 * 
	 * @param args Argumentos de línea de comandos.
	 */
	public static void main(String[] args) {
		SpringApplication.run(RemaPartnersApplication.class, args);
	}

}
