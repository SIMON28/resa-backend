package com.asptt.resa.commons.resource.mapper;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import com.asptt.resa.commons.exception.NotFoundException;

@Provider
public class NotFoundMapper implements ExceptionMapper<NotFoundException> {

	@Override
	public Response toResponse(NotFoundException ex) {
		return ErrorResponse.build(ex);
	}
}
