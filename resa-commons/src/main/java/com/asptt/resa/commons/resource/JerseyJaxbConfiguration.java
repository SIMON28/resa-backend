package com.asptt.resa.commons.resource;

import org.glassfish.jersey.jackson.JacksonFeature;
import org.glassfish.jersey.server.ResourceConfig;

import com.asptt.resa.commons.resource.mapper.BadUsageMapper;
import com.asptt.resa.commons.resource.mapper.FunctionalMapper;
import com.asptt.resa.commons.resource.mapper.JsonMappingMapper;
import com.asptt.resa.commons.resource.mapper.NotFoundMapper;
import com.asptt.resa.commons.resource.mapper.TechnicalMapper;
import com.asptt.resa.commons.resource.mapper.UnauthorizedMapper;
import com.asptt.resa.commons.resource.mapper.UnhandledMapper;

public abstract class JerseyJaxbConfiguration extends ResourceConfig {

	public JerseyJaxbConfiguration() {

		register(JacksonFeature.class);
		
		register(JacksonJaxbFormatProvider.class);

		register(BadUsageMapper.class);
		register(FunctionalMapper.class);
		register(JsonMappingMapper.class);
		register(NotFoundMapper.class);
		register(TechnicalMapper.class);
		register(UnauthorizedMapper.class);
		register(UnhandledMapper.class);

	}

}
