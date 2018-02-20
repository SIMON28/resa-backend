package com.asptt.resa.commons.json;

import java.util.HashMap;
import java.util.Map;

public class MappingTable {

	private Map<String, String> rows = new HashMap<>();

	public MappingTable map(String from, String to) {
		rows.put(from, to);
		return this;
	}

	public Map<String, String> getRows() {
		return rows;
	}

}
