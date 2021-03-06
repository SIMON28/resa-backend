package com.asptt.resa.commons.resource;

import javax.ws.rs.ApplicationPath;

import org.glassfish.jersey.jackson.JacksonFeature;
import org.glassfish.jersey.server.ResourceConfig;
import org.springframework.context.annotation.Configuration;

import com.asptt.resa.commons.resource.mapper.AccessDeniedMapper;
import com.asptt.resa.commons.resource.mapper.BadUsageMapper;
import com.asptt.resa.commons.resource.mapper.FunctionalMapper;
import com.asptt.resa.commons.resource.mapper.JsonMappingMapper;
import com.asptt.resa.commons.resource.mapper.NotFoundMapper;
import com.asptt.resa.commons.resource.mapper.NotImplementedMapper;
import com.asptt.resa.commons.resource.mapper.TechnicalMapper;
import com.asptt.resa.commons.resource.mapper.UnauthorizedMapper;
import com.asptt.resa.commons.resource.mapper.UnhandledMapper;
import com.asptt.resabackend.resources.adherent.AdherentResourceImpl;
import com.asptt.resabackend.resources.plongee.PlongeeResourceImpl;

@Configuration
@ApplicationPath("/api/v1")
//public class JerseyConfiguration extends JerseyJaxbConfiguration  {
public class JerseyConfiguration extends ResourceConfig  {

	public JerseyConfiguration() {
		super();
//                this.register(YourBackendJerseyConfiguration.class);
                
        		this.register(JacksonJaxbFormatProvider.class);

                this.register(JacksonFeature.class);
                this.register(BadUsageMapper.class);
                this.register(FunctionalMapper.class);
                this.register(JsonMappingMapper.class);
                this.register(NotFoundMapper.class);
                this.register(TechnicalMapper.class);
                this.register(UnauthorizedMapper.class);
                this.register(UnhandledMapper.class);
                this.register(AccessDeniedMapper.class);
                this.register(NotImplementedMapper.class);
                //resources
        		this.register(AdherentResourceImpl.class);
        		this.register(PlongeeResourceImpl.class);
                
	}
}
