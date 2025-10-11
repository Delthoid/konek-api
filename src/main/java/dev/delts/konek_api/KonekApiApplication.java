package dev.delts.konek_api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class KonekApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(KonekApiApplication.class, args);
	}

}
