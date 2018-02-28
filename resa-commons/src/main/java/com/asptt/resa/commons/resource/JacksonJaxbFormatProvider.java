package com.asptt.resa.commons.resource;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

import javax.ws.rs.ext.ContextResolver;
import javax.ws.rs.ext.Provider;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.AnnotationIntrospector;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.introspect.JacksonAnnotationIntrospector;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.fasterxml.jackson.module.jaxb.JaxbAnnotationIntrospector;

@Provider
public class JacksonJaxbFormatProvider implements ContextResolver<ObjectMapper> {

	final ObjectMapper defaultObjectMapper;

	public JacksonJaxbFormatProvider() {
		this.defaultObjectMapper = JacksonJaxbFormatProvider.createDefaultMapper();
	}

	@Override
	public ObjectMapper getContext(final Class<?> type) {
		return this.defaultObjectMapper;
	}

	protected static ObjectMapper createDefaultMapper() {
		final ObjectMapper mapper = new ObjectMapper();
		
		// We use JAXB annot
		AnnotationIntrospector jaxbIntrospector = new JaxbAnnotationIntrospector(TypeFactory.defaultInstance());
		// We also use Jackson annotations :)
		AnnotationIntrospector jacksonIntrospector = new JacksonAnnotationIntrospector();
		mapper.setAnnotationIntrospector(AnnotationIntrospector.pair(jaxbIntrospector, jacksonIntrospector));

		// annotations
		mapper.enable(MapperFeature.USE_ANNOTATIONS);

		// Ignore unknow properties
		mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		// serialization des timestamp
		mapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
		
		// Ignore null
		mapper.setSerializationInclusion(Include.NON_NULL);
		// Ignore empty
		mapper.setSerializationInclusion(Include.NON_EMPTY);
		// fixe format date
		final DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
		mapper.setDateFormat(df);
		
		return mapper;
	}

}
