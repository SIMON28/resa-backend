package com.asptt.resa.commons.resource.mapper;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;

import org.springframework.security.access.AccessDeniedException;

import com.asptt.resa.commons.exception.AccessDenied;

public class AccessDeniedMapper implements ExceptionMapper<AccessDeniedException> {

	@Override
	public Response toResponse(AccessDeniedException exception) {
		com.asptt.resa.commons.exception.AccessDeniedException ex = 
				new com.asptt.resa.commons.exception.AccessDeniedException(AccessDenied.GENERIC, exception.getMessage());
		return ErrorResponse.build(ex);
	}

}
