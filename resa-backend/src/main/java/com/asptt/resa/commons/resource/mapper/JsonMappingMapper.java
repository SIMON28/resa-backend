/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.asptt.resa.commons.resource.mapper;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import com.fasterxml.jackson.databind.JsonMappingException;
import com.asptt.resa.commons.exception.BadUsage;

@Provider
public class JsonMappingMapper
		implements ExceptionMapper<JsonMappingException> {
	@Override
	public Response toResponse(JsonMappingException ex) {
		JsonError error = new JsonError(BadUsage.UNKNOWN_VALUE.getValue(),
				ex.getMessage());
		return Response.status(Response.Status.BAD_REQUEST.getStatusCode())
				.entity(error).type(MediaType.APPLICATION_JSON).build();
	}
}
