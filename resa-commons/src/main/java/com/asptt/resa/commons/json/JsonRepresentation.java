package com.asptt.resa.commons.json;

import java.util.LinkedHashSet;
import java.util.Set;

public class JsonRepresentation {

	private Set<String> attributes = new LinkedHashSet<>();

	public JsonRepresentation() {
	}

	public JsonRepresentation(Set<String> attributes) {
		this.attributes.addAll(attributes);
	}

	public JsonRepresentation add(String attributePath) {
		this.attributes.add(attributePath);
		return this;
	}

	public JsonRepresentation add(JsonRepresentation jsonRepresentation) {
		this.attributes.addAll(jsonRepresentation.getAttributes());
		return this;
	}

	public Set<String> getAttributes() {
		return attributes;
	}

}
