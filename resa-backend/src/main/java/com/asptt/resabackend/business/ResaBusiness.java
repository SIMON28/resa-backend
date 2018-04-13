package com.asptt.resabackend.business;

import javax.ws.rs.core.UriInfo;

import com.asptt.resabackend.entity.Plongee;

public interface ResaBusiness {

	boolean plongeeIsOuverte(Plongee plongee);

	void isOkForResa(Integer plongeeId, String adherentId);
	/**
	 * Verifie si l'order est correct et en fonction de l'action continu la reservation
	 * @param uriInfo
	 * @param plongeeId
	 * @param order
	 */
	void checkOrderForDive(UriInfo uriInfo, Integer plongeeId, OrderForDive order);
	
}

