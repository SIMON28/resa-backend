package com.asptt.resa.commons.resource.mapper;

import com.asptt.resa.commons.exception.FunctionalException;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class FunctionalMapper implements ExceptionMapper<FunctionalException> {

	@Override
	public Response toResponse(FunctionalException ex) {
		return ErrorResponse.build(ex);
	}
}
