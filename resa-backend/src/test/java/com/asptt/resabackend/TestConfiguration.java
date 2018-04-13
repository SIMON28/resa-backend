package com.asptt.resabackend;

import javax.sql.DataSource;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;


/**
 * Configuration de l'application pour un serveur tournant en local.
 * 
 * @author clxq1935
 *
 */
@Configuration
@PropertySource({"classpath:application-test.properties","classpath:properties/ResaPlongee.properties","classpath:properties/InscriptionPlongeePage.properties"})
@ConfigurationProperties(prefix = "spring.datasource")
public class TestConfiguration {

	@Bean
	@ConfigurationProperties(prefix="spring.datasource")
	public DataSource dataSource() {
	    return DataSourceBuilder.create().build();
	}
	
	@Bean
	public static PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer() {
	  return new PropertySourcesPlaceholderConfigurer();
	}

}
