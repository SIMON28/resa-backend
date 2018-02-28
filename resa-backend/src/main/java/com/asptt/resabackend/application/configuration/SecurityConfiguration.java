package com.asptt.resabackend.application.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;

import com.asptt.resa.commons.exception.UnauthorizedException;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {
	
	@Override
	protected void configure(HttpSecurity http) throws Exception, UnauthorizedException {
		http
			.sessionManagement()
			.sessionCreationPolicy(SessionCreationPolicy.NEVER);
		
		http
			.antMatcher("/api/**")
			.authorizeRequests()
				.anyRequest().authenticated()
				.and()
			.httpBasic();
			
		
//        http.authorizeRequests().anyRequest().fullyAuthenticated();
//        http.httpBasic().authenticationEntryPoint(authEntryPoint);
//		http.csrf().disable();

//		http.csrf().disable().authorizeRequests() .anyRequest().authenticated() .and().httpBasic() .authenticationEntryPoint(authEntryPoint);
	}
	
	@Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth
            .inMemoryAuthentication()
                .withUser("user").password("password").roles("USER");
    }
}