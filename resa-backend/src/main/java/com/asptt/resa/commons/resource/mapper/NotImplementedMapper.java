package com.asptt.resa.commons.resource.mapper;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import com.asptt.resa.commons.exception.NotImplementedException;

@Provider
public class NotImplementedMapper  implements ExceptionMapper<NotImplementedException> {

	@Override
	public Response toResponse(NotImplementedException ex) {
		return ErrorResponse.build(ex);
	}
}
