package com.asptt.resabackend.resources.plongee;

import com.asptt.resa.commons.json.JsonRepresentation;
import com.asptt.resa.commons.json.MappingTable;
import com.asptt.resa.commons.service.EditionPolicy;

public class PlongeeSpecification {

	private static JsonRepresentation plongeeLightView;

	private static JsonRepresentation plongeeFullView;

	private static EditionPolicy editionPolicy;

	private static MappingTable mappingTable;

	private PlongeeSpecification() {
	}

	public static JsonRepresentation getPlongeeLightView() {
		if (plongeeLightView == null) {
			plongeeLightView = new JsonRepresentation().add("numeroLicense").add("nom")
					.add("prenom").add("mail").add("niveau").add("telephone");
		}
		return plongeeLightView;
	}

	public static JsonRepresentation getPlongeeFullView() {
		if (plongeeFullView == null) {
			plongeeFullView = new JsonRepresentation().add(getPlongeeLightView())
					.add("encadrement").add("aptitude").add("pilote").add("tiv").add("dp").
					add("actif").add("password").add("commentaire").add("dateCM").add("anneeCotisation").add("roles").add("contacts");
		}
		return plongeeFullView;
	}

	public static EditionPolicy getEditionPolicy() {
		if (editionPolicy == null) {
			editionPolicy = new EditionPolicy().auth("firstName")
					.auth("lastName").auth("room");
		}
		return editionPolicy;
	}

	public static MappingTable getMappingTable() {
		if (mappingTable == null) {
			mappingTable = new MappingTable().map("project.id", "projects.id")
					.map("document.id", "projects.document.id");
		}
		return mappingTable;
	}

}
