package com.asptt.resabackend.resources;

import javax.ws.rs.ApplicationPath;

import org.springframework.context.annotation.Configuration;

import com.asptt.resa.commons.resource.JerseyConfiguration;
import com.asptt.resabackend.resources.adherent.AdherentResourceImpl;

@Configuration
@ApplicationPath("/api/0.1")
public class ResourcesConfiguration extends JerseyConfiguration {

	public ResourcesConfiguration() {
		super();
		this.register(AdherentResourceImpl.class);
//		this.register(MonitorResourceImpl.class);
//		this.register(UserResourceImpl.class);
//		this.register(ProjectResourceImpl.class);
//		this.register(DocumentResourceImpl.class);
//		this.register(ServiceOrderResourceImpl.class);
	}
}
