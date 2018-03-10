package com.asptt.resabackend.application.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configurers.provisioning.InMemoryUserDetailsManagerConfigurer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.asptt.resa.commons.exception.UnauthorizedException;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

	@Autowired
	private SecurityEntryPoint securityEntryPoint;

	@Override
	protected void configure(HttpSecurity http) throws Exception, UnauthorizedException {
		// http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.NEVER);

		// http.antMatcher("/api/**").authorizeRequests().anyRequest().authenticated().and().httpBasic();
		// http.csrf().disable();

		http.csrf().disable();

		// All requests send to the Web Server request must be authenticated
		http.authorizeRequests().anyRequest().authenticated();

		// Use AuthenticationEntryPoint to authenticate user/password
		http.httpBasic().authenticationEntryPoint(securityEntryPoint);

		// http.authorizeRequests()
		// .antMatchers("api/**").hasRole("USER").and().httpBasic();

		// http.authorizeRequests().anyRequest().fullyAuthenticated();
		// http.httpBasic().authenticationEntryPoint(authEntryPoint);

		// http.csrf().disable().authorizeRequests() .anyRequest().authenticated()
		// .and().httpBasic() .authenticationEntryPoint(authEntryPoint);
	}

	 @Bean
	 public BCryptPasswordEncoder passwordEncoder() {
	 BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
	 return bCryptPasswordEncoder;
	 }

	@Autowired
	public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
		// auth.inMemoryAuthentication().withUser("user").password("password").roles("USER");

		String password = "password";

		String encrytedPassword = this.passwordEncoder().encode(password);
		System.out.println("Encoded password of password=" + encrytedPassword);
		
		InMemoryUserDetailsManagerConfigurer<AuthenticationManagerBuilder> //
		mngConfig = auth.inMemoryAuthentication();

		UserDetails u1 = User.withUsername("user").password(encrytedPassword).roles("USER").build();
		mngConfig.withUser(u1);

	}
}
