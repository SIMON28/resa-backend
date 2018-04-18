package com.asptt.resabackend.application.configuration;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.AuthenticationEntryPoint;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

	@Override
	public void configure(WebSecurity web) throws Exception {
		// TODO Auto-generated method stub
		super.configure(web);
	}

	@Autowired
	private SecurityEntryPoint securityEntryPoint;

	// @Override
	// protected void configure(HttpSecurity http) throws Exception,
	// UnauthorizedException {
	// http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.NEVER);
	//
	// http.antMatcher("/api/**").authorizeRequests().anyRequest().authenticated().and().httpBasic();
	// http.csrf().disable();
	// // CONFIG Spring BOOT 2.0
	// // http.csrf().disable();
	// // // All requests send to the Web Server request must be authenticated
	// // http.authorizeRequests().anyRequest().authenticated();
	// // // Use AuthenticationEntryPoint to authenticate user/password
	// // http.httpBasic().authenticationEntryPoint(securityEntryPoint);
	// }
	// @Override
	// protected void configure(final HttpSecurity http) throws Exception {
	// http.csrf().disable().sessionManagement()
	// .sessionCreationPolicy(SessionCreationPolicy.STATELESS).and().httpBasic()
	// .disable().authorizeRequests().anyRequest().authenticated().and()
	// .anonymous().disable().exceptionHandling()
	// .authenticationEntryPoint(unauthorizedEntryPoint());
	//
	//// http.addFilterBefore(new AuthenticationFilter(authenticationManager()),
	//// BasicAuthenticationFilter.class);
	// }
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
		http.antMatcher("/api/**").authorizeRequests().anyRequest().authenticated().and().httpBasic();
		http.exceptionHandling().authenticationEntryPoint(unauthorizedEntryPoint());
		// http.anonymous().disable().exceptionHandling().authenticationEntryPoint(securityEntryPoint);
		http.csrf().disable();
	}

	@Bean
	public AuthenticationEntryPoint unauthorizedEntryPoint() {
		return (request, response, authException) -> response.sendError(
				// HttpServletResponse.SC_UNAUTHORIZED, authException.getMessage());
				HttpServletResponse.SC_UNAUTHORIZED, "MES GENOUX");
	}

	// @Bean
	// public BCryptPasswordEncoder passwordEncoder() {
	// BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
	// return bCryptPasswordEncoder;
	// }

	@Autowired
	public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
		auth.inMemoryAuthentication().withUser("user").password("password").roles("USER").and().withUser("admin")
				.password("password").roles("ADMIN");

		// CONFIG Spring BOOT 2.0
		// String password = "password";
		// String encrytedPassword = this.passwordEncoder().encode(password);
		// System.out.println("Encoded password of password=" + encrytedPassword);
		// InMemoryUserDetailsManagerConfigurer<AuthenticationManagerBuilder> //
		// mngConfig = auth.inMemoryAuthentication();
		// UserDetails u1 =
		// User.withUsername("user").password(encrytedPassword).roles("USER").build();
		// mngConfig.withUser(u1);

	}
}
