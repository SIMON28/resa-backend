package com.asptt.resabackend.resources.adherent;

import com.asptt.resa.commons.json.JsonRepresentation;
import com.asptt.resa.commons.json.MappingTable;
import com.asptt.resa.commons.service.EditionPolicy;

public class ContactUrgentSpecification {

	private static JsonRepresentation cuLightView;

	private static JsonRepresentation cuFullView;

	private static EditionPolicy editionPolicy;

	private static MappingTable mappingTable;

	private ContactUrgentSpecification() {
	}

	public static JsonRepresentation getContactUrgentLightView() {
		if (cuLightView == null) {
			cuLightView = new JsonRepresentation().add("id").add("nom")
					.add("prenom").add("titre").add("telephone").add("telephtwo");
		}
		return cuLightView;
	}

	public static JsonRepresentation getContactUrgentFullView() {
		if (cuFullView == null) {
			cuFullView = new JsonRepresentation().add(getContactUrgentLightView());
		}
		return cuFullView;
	}

	public static EditionPolicy getEditionPolicy() {
		if (editionPolicy == null) {
			editionPolicy = new EditionPolicy().auth("prenom")
					.auth("nom").auth("license");
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
