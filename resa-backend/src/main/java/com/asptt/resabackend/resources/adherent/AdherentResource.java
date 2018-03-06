package com.asptt.resabackend.resources.adherent;

import java.util.List;

import javax.ws.rs.PathParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import com.asptt.resa.commons.resource.Resource;
import com.asptt.resabackend.entity.Adherent;
import com.asptt.resabackend.entity.ContactUrgent;

public interface AdherentResource extends Resource<Adherent> {

	Response findContacts(final @Context UriInfo uriInfo,
			final @PathParam("adherentId") String adherentId);

	Response createContacts(UriInfo uriInfo, String adherentId, List<ContactUrgent> resources);

	Response deleteContacts(UriInfo uriInfo, String adherentId);

}