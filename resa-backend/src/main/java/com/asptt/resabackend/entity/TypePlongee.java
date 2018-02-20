/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.asptt.resabackend.entity;

public enum TypePlongee {
    MATIN("MATIN"),
    APRES_MIDI("APRES_MIDI"),
    SOIR("SOIR"),
    NUIT("NUIT"),
    LIBRE("LIBRE");

    private final String text;

    TypePlongee(String text) {
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
    public static TypePlongee fromString(String text) {
        if (text != null) {
            for (TypePlongee b : TypePlongee.values()) {
                if (text.equalsIgnoreCase(b.text)) {
                    return b;
                }
            }
        }
        return null;
    }

}
