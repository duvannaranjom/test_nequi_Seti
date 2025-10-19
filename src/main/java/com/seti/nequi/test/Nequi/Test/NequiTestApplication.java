package com.seti.nequi.test.Nequi.Test;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@OpenAPIDefinition(
		info = @Info(
				title = "API creada para la gestión de Franquicias",
				version = "1.0",
				description = "API que permite la gestión de franquicias, sucursales y productos"
		)
)
public class NequiTestApplication {

	public static void main(String[] args) {
		SpringApplication.run(NequiTestApplication.class, args);
	}

}
