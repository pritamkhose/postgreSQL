package com.pritam.postgreSQL.config;

import javax.sql.DataSource;

import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

// https://www.baeldung.com/spring-boot-configure-data-source-programmatic
// https://stackoverflow.com/questions/28821521/configure-datasource-programmatically-in-spring-boot

// https://stackoverflow.com/questions/44803211/read-environment-variable-in-springboot/44803417
// 
@Configuration
public class JpaConfig {

	@Bean
	public DataSource getDataSource() {
		DataSourceBuilder dataSourceBuilder = DataSourceBuilder.create();
		dataSourceBuilder.driverClassName("org.postgresql.Driver");

		/*
		 * Run command line cmd run jar file = java
		 * -DDBurl=jdbc:postgresql://localhost:5432/postgres_demo -DDBusername=postgres
		 * -DDBpassword=root -jar postgreSQL-0.0.1-SNAPSHOT.jar
		 */
		
		// Run in Heroku production Java
		// https://devcenter.heroku.com/articles/config-vars
// 		dataSourceBuilder.url(System.getenv("DBurl"));
// 		dataSourceBuilder.username(System.getenv("DBusername"));
// 		dataSourceBuilder.password(System.getenv("DBpassword"));
		
		// Run in Heroku production Java where DATABASE_URL change dyanamically by Heroku postgreSQL
		String DATABASE_URL = System.getenv("DATABASE_URL");
		String [] DbArr = DATABASE_URL.split("/", -1);
		String DBurl = "jdbc:postgresql://" + DATABASE_URL.split("@", -1)[1];
		String DBusername = DbArr[2].split(":", -1)[0]; 
		String DBpassword = DbArr[2].split(":", -1)[1].split("@", -1)[0];
		dataSourceBuilder.url(DBurl);
		dataSourceBuilder.username(DBusername);
		dataSourceBuilder.password(DBpassword);

		// Run in CMD Java
// 		dataSourceBuilder.url(System.getProperty("DBurl"));
// 		dataSourceBuilder.username(System.getProperty("DBusername"));
// 		dataSourceBuilder.password(System.getProperty("DBpassword"));

		// Run in Eclipse
//		dataSourceBuilder.url("jdbc:postgresql://localhost:5432/postgres_demo");
//		dataSourceBuilder.username("postgres");
//		dataSourceBuilder.password("root");

		return dataSourceBuilder.build();

	}
}
