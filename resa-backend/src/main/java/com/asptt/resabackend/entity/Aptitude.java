/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.asptt.resabackend.entity;

public enum Aptitude {

    neant(""),
    PE40("PE40"),
    PE60("PE60");

    private final String text;

    Aptitude(String text) {
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
    public static Aptitude fromString(String text) {
        if (text != null) {
            for (Aptitude b : Aptitude.values()) {
                if (text.equalsIgnoreCase(b.text)) {
                    return b;
                }
            }
        }
        return null;
    }

}
