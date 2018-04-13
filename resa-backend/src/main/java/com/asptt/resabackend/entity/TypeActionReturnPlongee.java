/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.asptt.resabackend.entity;

public enum TypeActionReturnPlongee {
    CONSULTER("consulter"),
    RESERVER("reserver"),
    DESINSCRIRE("desinscrire");

    private final String text;

    TypeActionReturnPlongee(String text) {
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
    public static TypeActionReturnPlongee fromString(String text) {
        if (text != null) {
            for (TypeActionReturnPlongee b : TypeActionReturnPlongee.values()) {
                if (text.equalsIgnoreCase(b.text)) {
                    return b;
                }
            }
        }
        return null;
    }

}
