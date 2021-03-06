package com.asptt.resabackend.business;

public enum TypeOrderForDive {
    ADD("add"),
    DELETE("delete"),
    WAIT("wait");

    private final String text;

    TypeOrderForDive(String text) {
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
    public static TypeOrderForDive fromString(String text) {
        if (text != null) {
            for (TypeOrderForDive b : TypeOrderForDive.values()) {
                if (text.equalsIgnoreCase(b.text)) {
                    return b;
                }
            }
        }
        return null;
    }


}
