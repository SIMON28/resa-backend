package com.asptt.resa.commons.resource.mapper;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import com.asptt.resa.commons.exception.UnauthorizedException;

@Provider
public class UnauthorizedMapper
		implements ExceptionMapper<UnauthorizedException> {

	@Override
	public Response toResponse(UnauthorizedException ex) {
		return ErrorResponse.build(ex);
	}
}
