package com.asptt.resa.commons.resource.mapper;

import java.nio.file.AccessDeniedException;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;

import org.glassfish.jersey.spi.ExtendedExceptionMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.asptt.resa.commons.exception.AccessDenied;
import com.asptt.resa.commons.exception.Technical;


@Provider
public class UnhandledMapper implements ExtendedExceptionMapper<Throwable> {

	private static final Logger LOGGER = LoggerFactory
			.getLogger(UnhandledMapper.class);

	@Override
	public Response toResponse(Throwable exception) {
		if(exception instanceof AccessDeniedException) {
			com.asptt.resa.commons.exception.AccessDeniedException accessDeniedEx = 
					new com.asptt.resa.commons.exception.AccessDeniedException(AccessDenied.GENERIC, exception.getMessage());
			return ErrorResponse.build(accessDeniedEx);
		} else {
		
		LOGGER.error(
				"Code RED, this is an unhandled error, you have to fix your code, see below:",
				exception);
		JsonError error = new JsonError(Technical.GENERIC.getValue(),
				"We re sorry, things went very bad, help us by providing some feedback about the context, please contact us");
		return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
				.entity(error).type(MediaType.APPLICATION_JSON).build();
		}
	}

	@Override
	public boolean isMappable(Throwable throwable) {
		return !(throwable instanceof WebApplicationException);
	}

}
