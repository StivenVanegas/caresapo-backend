package lol.caresapo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class CaresapoApplication {

	public static void main(String[] args) {
		SpringApplication.run(CaresapoApplication.class, args);
	}

}
