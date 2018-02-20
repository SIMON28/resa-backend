package com.asptt.resabackend;

import javax.sql.DataSource;

import org.springframework.boot.autoconfigure.jdbc.DataSourceBuilder;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;


/**
 * Configuration de l'application pour un serveur tournant en local.
 * 
 * @author clxq1935
 *
 */
@Configuration
@Profile(value = "test")
@PropertySource(value={"classpath:application-test.properties"})
public class TestConfiguration {

	@Bean
	@ConfigurationProperties(prefix="test.datasource")
	public DataSource dataSource() {
	    return DataSourceBuilder.create().build();
	}
	
	@Bean
	public static PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer() {
	  return new PropertySourcesPlaceholderConfigurer();
	}	
}
