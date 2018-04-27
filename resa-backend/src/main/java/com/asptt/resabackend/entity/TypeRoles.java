package com.asptt.resabackend.entity;

public enum TypeRoles {
	ADMIN("ADMIN"),
	USER("USER"),
	SECRETARIAT("SECRETARIAT"),
	DP("DP"),
	ENCADRANT("ENCADRANT");

    private final String text;

    TypeRoles(String text) {
        this.text = text;
    }

    /**
     *
     * @return
     */
    public String getText() {
        return this.text;
    }

    /**
     *
     * @param text
     * @return
     */
    public static TypeRoles fromString(String text) {
        if (text != null) {
            for (TypeRoles b : TypeRoles.values()) {
                if (text.equalsIgnoreCase(b.text)) {
                    return b;
                }
            }
        }
        return null;
    }
}
