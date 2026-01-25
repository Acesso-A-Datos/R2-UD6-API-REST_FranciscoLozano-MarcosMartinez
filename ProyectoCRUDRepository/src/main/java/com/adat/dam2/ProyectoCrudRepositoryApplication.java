package com.adat.dam2;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/*
 * @SpringBootApplication
 * Es la anotación MÁS IMPORTANTE en una aplicación Spring Boot.
 * Marca esta clase como el punto de arranque del proyecto.
 *
 * Internamente agrupa TRES anotaciones:
 *
 * 1) @Configuration
 *    - Indica que esta clase puede definir beans de Spring
 *    - Es como decir: "aquí hay configuración"
 *
 * 2) @EnableAutoConfiguration
 *    - Spring analiza las dependencias del proyecto (JPA, Web, Security, etc.)
 *    - Configura automáticamente lo necesario
 *    - Ejemplo: si detecta Spring Web, configura Tomcat y MVC
 *
 * 3) @ComponentScan
 *    - Spring busca automáticamente clases con:
 *      @Component, @Service, @Repository, @Controller, @RestController
 *    - SOLO dentro de este paquete y sus subpaquetes
 *
 *  Por eso este paquete suele ser el "paquete raíz" del proyecto
 */
@SpringBootApplication
public class ProyectoCrudRepositoryApplication {

	public static void main(String[] args) {
		
		/*
		 * SpringApplication.run(...)
		 * Arranca toda la aplicación Spring Boot:
		 * - Crea el ApplicationContext
		 * - Escanea componentes (@ComponentScan)
		 * - Configura beans automáticamente
		 * - Arranca el servidor embebido (Tomcat)
		 * - Deja la app lista para recibir peticiones HTTP
		 * args -> argumentos de consola (si los hubiera)
		 */
		SpringApplication.run(ProyectoCrudRepositoryApplication.class, args);
	}

}
