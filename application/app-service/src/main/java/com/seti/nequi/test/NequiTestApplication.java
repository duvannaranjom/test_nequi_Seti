package com.seti.nequi.test;


//import com.seti.nequi.test.infrastructure.entrypoints.web.error.GlobalExceptionHandler;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;

@SpringBootApplication(scanBasePackages = "com.seti.nequi.test")
//@Import(GlobalExceptionHandler.class)
public class NequiTestApplication {
	public static void main(String[] args) {
		SpringApplication.run(NequiTestApplication.class, args);
	}
}
