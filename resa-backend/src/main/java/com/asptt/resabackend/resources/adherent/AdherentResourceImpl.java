package com.asptt.resabackend.resources.adherent;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

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
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.UriInfo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestBody;

import com.asptt.resa.commons.annotation.PATCH;
import com.asptt.resa.commons.json.Jackson;
import com.asptt.resa.commons.json.JsonRepresentation;
import com.asptt.resa.commons.resource.ResourceBase;
import com.asptt.resa.commons.utils.URIParserUtils;
//import com.asptt.resabackend.commons.resource.ResourceBaseResa;
import com.asptt.resabackend.entity.Adherent;
import com.asptt.resabackend.entity.ContactUrgent;
import com.asptt.resabackend.entity.Plongee;
import com.asptt.resabackend.resources.contacturgent.ContactSpecification;
import com.asptt.resabackend.resources.plongee.PlongeeSpecification;
import com.fasterxml.jackson.databind.JsonNode;

@Path("adherent")
@Component("adherentResource")
public class AdherentResourceImpl extends ResourceBase<Adherent> implements AdherentResource{

	@Autowired
	private AdherentService service;

	@Override
	protected AdherentService getService() {
		return this.service;
	}
	
	@POST
	@Consumes({ MediaType.APPLICATION_JSON })
	@Produces({ MediaType.APPLICATION_JSON })
	@Override
	public Response create(final @Context UriInfo uriInfo,
			final @RequestBody Adherent resource) {
		return super.create(uriInfo, resource);
	}

	@GET
	@Path("{id}")
	@Produces({ MediaType.APPLICATION_JSON })
	@Override
	public Response get(final @Context UriInfo uriInfo,
			final @PathParam("id") String id) {
		Response resp = super.get(uriInfo, id); 
		return resp;
	}

	@GET
	@Produces({ MediaType.APPLICATION_JSON })
	@Override
	public Response find(final @Context UriInfo uriInfo) {
//		return super.find(uriInfo, AdherentSpecification.getAdherentLightView());
		return super.find(uriInfo, AdherentSpecification.getAdherentFullView());
//		return super.find(uriInfo);
	}

	 /* update full */
	 @PUT
	 @Path("{id}")
	 @Consumes({ MediaType.APPLICATION_JSON })
	 @Produces({ MediaType.APPLICATION_JSON })
	 @Override
	 public Response update(final @Context UriInfo uriInfo,
	 final @PathParam("id") String id,
	 final @RequestBody Adherent resource) {
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
//				AdherentSpecification.getAdherentFullView());
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
				AdherentSpecification.getAdherentFullView());
	}

	@GET
	@Path("{adherentId}/plongee")
	@Produces({ MediaType.APPLICATION_JSON })
	@Override
	public Response findPlongees(final @Context UriInfo uriInfo,
			final @PathParam("adherentId") String adherentId) {
		
		List<Plongee> plongees = this.getService().findPlongees(uriInfo, adherentId);
		
		final Object entities = constructPlongeeEntities(convPlongeeList(plongees), PlongeeSpecification.getPlongeeFullView());
		
		return Response.ok(entities).build();
	}
	
	
	@GET
	@Path("{adherentId}/contact")
	@Produces({ MediaType.APPLICATION_JSON })
	@Override
	public Response findContacts(final @Context UriInfo uriInfo,
			final @PathParam("adherentId") String adherentId) {
		
		List<ContactUrgent> contactUrgents = this.getService().findContacts(adherentId);
		
		final Object entities = constructContactUrgentEntities(convContactUrgentList(contactUrgents), ContactSpecification.getContactUrgentFullView());
		
		return Response.ok(entities).build();
	}
	
	@POST
	@Path("{adherentId}/contact")
	@Consumes({ MediaType.APPLICATION_JSON })
	@Produces({ MediaType.APPLICATION_JSON })
	@Override
	public Response createContacts(final @Context UriInfo uriInfo,
			final @PathParam("adherentId") String adherentId,
			final @RequestBody List<ContactUrgent> resources) {
		
		List<ContactUrgent> contactUrgents = this.getService().createContacts(adherentId, resources);
		
		final Object entities = constructContactUrgentEntities(convContactUrgentList(contactUrgents), ContactSpecification.getContactUrgentFullView());
		
		return Response.status(Status.CREATED).entity(entities).build();
	}

	@DELETE
	@Path("{adherentId}/contact")
	@Consumes({ MediaType.APPLICATION_JSON })
	@Produces({ MediaType.APPLICATION_JSON })
	@Override
	public Response deleteContacts(final @Context UriInfo uriInfo,
			final @PathParam("adherentId") String adherentId) {
		this.getService().deleteContacts(adherentId);
		return super.getResponse204();
	}

	protected Object constructContactUrgentEntities(final Set<ContactUrgent> resources, JsonRepresentation jsonRepresentation) {
		Object entities;
		Set<String> attributes = jsonRepresentation.getAttributes();
		if (attributes == null || attributes.isEmpty() || attributes.contains(URIParserUtils.ALL_FIELDS)) {
			entities = resources;
		} else {
			entities = Jackson.createNodes(resources, jsonRepresentation);
		}
		return entities;
	}
	
	/**
	 * Convert list to set
	 */
	public Set<ContactUrgent> convContactUrgentList(List<ContactUrgent> list) {
		if (list == null) {
			return new LinkedHashSet<>();
		} else {
			return new LinkedHashSet<>(list);
		}
	}

	protected Object constructPlongeeEntities(final Set<Plongee> resources, JsonRepresentation jsonRepresentation) {
		Object entities;
		Set<String> attributes = jsonRepresentation.getAttributes();
		if (attributes == null || attributes.isEmpty() || attributes.contains(URIParserUtils.ALL_FIELDS)) {
			entities = resources;
		} else {
			entities = Jackson.createNodes(resources, jsonRepresentation);
		}
		return entities;
	}
	
	/**
	 * Convert list to set
	 */
	public Set<Plongee> convPlongeeList(List<Plongee> list) {
		if (list == null) {
			return new LinkedHashSet<>();
		} else {
			return new LinkedHashSet<>(list);
		}
	}

}
