package com.nofaterock.restdocs;

import com.fasterxml.jackson.datatype.hibernate5.Hibernate5Module;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class SpringRestDocsExampleApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringRestDocsExampleApplication.class, args);
	}

	/**
	 * Hibernate Entity 를 Serialize 할 때 발생하는 JsonMappingException 을 해결하기 위함
	 */
	@Bean
	public Hibernate5Module datatypeHibernateModule() {
		return new Hibernate5Module();
	}

}
