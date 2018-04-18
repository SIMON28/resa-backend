package com.asptt.resabackend.application.configuration;

import javax.sql.DataSource;

import org.springframework.boot.autoconfigure.jdbc.DataSourceBuilder;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;


/**
 * Configuration de l'application pour un serveur tournant en local.
 * 
 * @author clxq1935
 *
 */
@Configuration
@Profile(value = "local")
@ComponentScan(basePackages= {"com.asptt.resabackend"})
public class LocalConfiguration {

	@Bean
	@ConfigurationProperties(prefix="local.datasource")
	public DataSource dataSource() {
	    return DataSourceBuilder.create().build();
	}
	
}
