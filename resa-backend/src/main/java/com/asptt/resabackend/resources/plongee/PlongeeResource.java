package com.asptt.resabackend.resources.plongee;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import com.asptt.resa.commons.resource.Resource;
import com.asptt.resabackend.business.OrderForDive;
import com.asptt.resabackend.business.ResaBusiness;
import com.asptt.resabackend.entity.Plongee;

public interface PlongeeResource extends Resource<Plongee> {

	ResaBusiness getResaBusiness();

	Response registerForDive(UriInfo uriInfo, Integer plongeeId, OrderForDive order);

}
