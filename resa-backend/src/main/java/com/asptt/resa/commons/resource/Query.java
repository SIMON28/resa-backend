package com.asptt.resa.commons.resource;

import javax.ws.rs.core.MultivaluedHashMap;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.UriInfo;

import com.asptt.resa.commons.json.JsonRepresentation;
import com.asptt.resa.commons.utils.URIParserUtils;

public class Query {

	private MultivaluedMap<String, String> criteria;

	private JsonRepresentation jsonRepresentation;

	public Query(UriInfo uriInfo) {
		this.criteria = new MultivaluedHashMap<String, String>(
				uriInfo.getQueryParameters());
		this.jsonRepresentation = new JsonRepresentation(
				URIParserUtils.getFields(criteria));
	}

	public MultivaluedMap<String, String> getQueryParameters() {
		return criteria;
	}

	public JsonRepresentation getRepresentation() {
		return jsonRepresentation;
	}

}
