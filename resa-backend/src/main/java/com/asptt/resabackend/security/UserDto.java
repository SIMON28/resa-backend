package com.asptt.resabackend.security;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotEmpty;

public class UserDto {
	@NotNull
    @NotEmpty
    private String license;
     
    @NotNull
    @NotEmpty
    private String password;
    private String matchingPassword;
    
    
	public String getLicense() {
		return license;
	}
	public void setLicense(String license) {
		this.license = license;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getMatchingPassword() {
		return matchingPassword;
	}
	public void setMatchingPassword(String matchingPassword) {
		this.matchingPassword = matchingPassword;
	}
     
}
