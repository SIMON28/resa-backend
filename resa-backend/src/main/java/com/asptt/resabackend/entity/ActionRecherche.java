/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.asptt.resabackend.entity;

public enum ActionRecherche {

    CONSULTATION("CONSULTATION"),
    RESERVATION("RESERVATION");

    private final String text;

    ActionRecherche(String text) {
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
    public static ActionRecherche fromString(String text) {
        if (text != null) {
            for (ActionRecherche b : ActionRecherche.values()) {
                if (text.equalsIgnoreCase(b.text)) {
                    return b;
                }
            }
        }
        return null;
    }

}
