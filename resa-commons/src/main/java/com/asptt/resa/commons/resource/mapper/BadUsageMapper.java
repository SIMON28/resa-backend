package com.asptt.resa.commons.resource.mapper;

import com.asptt.resa.commons.exception.BadUsageException;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class BadUsageMapper implements ExceptionMapper<BadUsageException> {

	@Override
	public Response toResponse(BadUsageException ex) {
		return ErrorResponse.build(ex);
	}

}
