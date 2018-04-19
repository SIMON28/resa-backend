package com.asptt.resabackend.resources;

import org.springframework.context.annotation.Configuration;

import com.asptt.resa.commons.resource.JerseyConfiguration;
import com.asptt.resabackend.resources.adherent.AdherentResourceImpl;
import com.asptt.resabackend.resources.plongee.PlongeeResourceImpl;

@Configuration
public class ResourcesConfiguration extends JerseyConfiguration {

	public ResourcesConfiguration() {
		super();
		this.register(AdherentResourceImpl.class);
		this.register(PlongeeResourceImpl.class);
//		this.register(SecurityFilter.class);
	}
}
