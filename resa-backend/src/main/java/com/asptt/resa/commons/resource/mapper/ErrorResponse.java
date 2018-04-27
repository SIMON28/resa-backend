package com.asptt.resa.commons.resource.mapper;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.asptt.resa.commons.exception.ApiException;

/**
 *
 * @author maig7313
 */
public class ErrorResponse {

	private ErrorResponse() {

	}

	public final static Response build(ApiException ex) {
		JsonError error = new JsonError(ex);
		return Response.status(ex.getCategory()).entity(error)
				.type(MediaType.APPLICATION_JSON).build();
	}

}
