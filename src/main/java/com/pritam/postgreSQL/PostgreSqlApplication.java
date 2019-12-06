package com.pritam.postgreSQL;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

// https://www.callicoder.com/spring-boot-jpa-hibernate-postgresql-restful-crud-api-example/
// https://github.com/callicoder/spring-boot-postgresql-jpa-hibernate-rest-api-demo
// http://127.0.0.1:50860/browser/

@SpringBootApplication
@EnableJpaAuditing
public class PostgreSqlApplication {

	public static void main(String[] args) {
		SpringApplication.run(PostgreSqlApplication.class, args);
	}

}
