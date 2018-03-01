package com.asptt.resa.commons.resource;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import com.asptt.resa.commons.json.JsonRepresentation;
import com.fasterxml.jackson.databind.JsonNode;

/**
 *
 * @param <R>
 */
public interface Resource<R, S> {

	public Response create(UriInfo uriInfo, R resource);

	public Response create(UriInfo uriInfo, R resource,
			JsonRepresentation jsonRepresentation);

	public Response get(UriInfo uriInfo, String id);

	// public T get(UriInfo uriInfo, String id) ;

	public Response get(UriInfo uriInfo, String id,
			JsonRepresentation jsonRepresentation);

	public Response find(UriInfo uriInfo);

	public Response find(UriInfo uriInfo,
			JsonRepresentation jsonRepresentation);

	public Response update(UriInfo uriInfo, String id, R resource);

	public Response update(UriInfo uriInfo, String id, R resource,
			JsonRepresentation jsonRepresentation);

	@Deprecated
	public Response updateWithNotNull(UriInfo uriInfo, String id, R resource);

	@Deprecated
	public Response updateWithNotNull(UriInfo uriInfo, String id, R resource,
			JsonRepresentation jsonRepresentation);

	public Response patch(UriInfo uriInfo, String id, JsonNode jsonPatch);

	public Response patch(UriInfo uriInfo, String id, JsonNode jsonPatch,
			JsonRepresentation jsonRepresentation);

	public Response merge(UriInfo uriInfo, String id,
			JsonNode jsonPartialTarget);

	public Response merge(UriInfo uriInfo, String id,
			JsonNode jsonPartialTarget, JsonRepresentation jsonRepresentation);

	public Response diff(UriInfo uriInfo, String id, JsonNode jsonTarget);

	public Response delete(UriInfo uriInfo, String id);
}
