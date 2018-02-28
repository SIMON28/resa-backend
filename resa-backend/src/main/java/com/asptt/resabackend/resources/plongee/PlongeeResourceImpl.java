package com.asptt.resabackend.resources.plongee;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestBody;

import com.asptt.resa.commons.annotation.PATCH;
import com.asptt.resa.commons.resource.ResourceBase;
//import com.asptt.resabackend.commons.resource.ResourceBaseResa;
import com.asptt.resabackend.entity.Plongee;
import com.fasterxml.jackson.databind.JsonNode;

@Path("plongee")
@Component("plongeeResource")
public class PlongeeResourceImpl extends ResourceBase<Plongee> {

	@Autowired
	private PlongeeService service;

	@Override
	protected PlongeeService getService() {
		return this.service;
	}
	
	@POST
	@Consumes({ MediaType.APPLICATION_JSON })
	@Produces({ MediaType.APPLICATION_JSON })
	@Override
	public Response create(final @Context UriInfo uriInfo,
			final @RequestBody Plongee resource) {
		return super.create(uriInfo, resource);
	}

	@GET
	@Path("{id}")
	@Produces({ MediaType.APPLICATION_JSON })
	@Override
	public Response get(final @Context UriInfo uriInfo,
			final @PathParam("id") String id) {
		return super.get(uriInfo, id);
	}

	@GET
	@Produces({ MediaType.APPLICATION_JSON })
	@Override
	public Response find(final @Context UriInfo uriInfo) {
//		return super.find(uriInfo, PlongeeSpecification.getPlongeeLightView());
		return super.find(uriInfo, PlongeeSpecification.getPlongeeFullView());
	}

	 /* update full */
	 @PUT
	 @Path("{id}")
	 @Consumes({ MediaType.APPLICATION_JSON })
	 @Produces({ MediaType.APPLICATION_JSON })
	 @Override
	 public Response update(final @Context UriInfo uriInfo,
	 final @PathParam("id") String id,
	 final @RequestBody Plongee resource) {
	 return super.update(uriInfo, id, resource);
	 }

	/* update, merge */
//	@PUT
//	@Path("{id}")
//	@Consumes({ MediaType.APPLICATION_JSON })
//	@Produces({ MediaType.APPLICATION_JSON })
//	@Override
//	public Response merge(final @Context UriInfo uriInfo,
//			final @PathParam("id") String id,
//			final @RequestBody JsonNode partialResource) {
//		return super.merge(uriInfo, id, partialResource,
//				PlongeeSpecification.getPlongeeFullView());
//	}

	@DELETE
	@Path("{id}")
	@Consumes({ MediaType.APPLICATION_JSON })
	@Produces({ MediaType.APPLICATION_JSON })
	@Override
	public Response delete(final @Context UriInfo uriInfo,
			final @PathParam("id") String id) {
		return super.delete(uriInfo, id);
	}

	@POST
	@Path("{id}/diff")
	@Consumes({ MediaType.APPLICATION_JSON })
	@Produces({ MediaType.APPLICATION_JSON })
	@Override
	public Response diff(final @Context UriInfo uriInfo,
			final @PathParam("id") String id,
			final @RequestBody JsonNode targetResource) {
		return super.diff(uriInfo, id, targetResource);
	}

	@PATCH
	@Path("{id}")
	@Consumes({ "application/json-patch+json" })
	@Produces({ MediaType.APPLICATION_JSON })
	@Override
	public Response patch(final @Context UriInfo uriInfo,
			final @PathParam("id") String id,
			final @RequestBody JsonNode jsonPatch) {
		return super.patch(uriInfo, id, jsonPatch,
				PlongeeSpecification.getPlongeeFullView());
	}

}
