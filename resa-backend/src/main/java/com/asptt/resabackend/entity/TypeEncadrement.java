package com.asptt.resabackend.entity;

public enum TypeEncadrement {
    E2("E2"),
    E3("E3"),
    E4("E4");

    private final String text;

    TypeEncadrement(String text) {
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
    public static TypeEncadrement fromString(String text) {
        if (text != null) {
            for (TypeEncadrement b : TypeEncadrement.values()) {
                if (text.equalsIgnoreCase(b.text)) {
                    return b;
                }
            }
        }
        return null;
    }


}
