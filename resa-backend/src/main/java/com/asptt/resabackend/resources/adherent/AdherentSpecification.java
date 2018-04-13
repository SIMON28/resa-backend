package com.asptt.resabackend.resources.adherent;

import org.springframework.stereotype.Component;

import com.asptt.resa.commons.json.JsonRepresentation;
import com.asptt.resa.commons.json.MappingTable;
import com.asptt.resa.commons.service.EditionPolicy;

@Component
public class AdherentSpecification {

	private static JsonRepresentation adherentLightView;

	private static JsonRepresentation adherentFullView;

	private static EditionPolicy editionPolicy;

	private static MappingTable mappingTable;

	private AdherentSpecification() {
	}

	public static JsonRepresentation getAdherentLightView() {
		if (adherentLightView == null) {
			adherentLightView = new JsonRepresentation().add("numeroLicense").add("nom")
					.add("prenom").add("mail").add("niveau").add("telephone");
		}
		return adherentLightView;
	}

	public static JsonRepresentation getAdherentFullView() {
		if (adherentFullView == null) {
			adherentFullView = new JsonRepresentation().add(getAdherentLightView())
					.add("encadrement").add("aptitude").add("pilote").add("tiv").add("dp").
					add("actif").add("password").add("commentaire").add("dateCM").add("anneeCotisation").add("roles").add("contacts");
		}
		return adherentFullView;
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
