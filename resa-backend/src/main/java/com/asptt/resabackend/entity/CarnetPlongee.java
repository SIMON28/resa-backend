/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.asptt.resabackend.entity;

import java.io.Serializable;

/**
 *
 * @author ECUS6396
 */
public class CarnetPlongee implements Serializable {

    private static final long serialVersionUID = -8569233748603210161L;
    public static enum TypePlongee {
        EXPLO, EXERCICE, EPAVE, NUIT
    }
    public static enum TypeInstrument {
        ORDI, TABLE
    }

    private Integer id;
    private Integer idFS;
    private Integer idPlongee;
    private Integer idPalanque;
    
    private String site;
    private String meteo;
    private TypePlongee typePlongee;
    private TypeInstrument typeInstrument;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getIdFS() {
        return idFS;
    }

    public void setIdFS(Integer idFS) {
        this.idFS = idFS;
    }

    public Integer getIdPlongee() {
        return idPlongee;
    }

    public void setIdPlongee(Integer idPlongee) {
        this.idPlongee = idPlongee;
    }

    public Integer getIdPalanque() {
        return idPalanque;
    }

    public void setIdPalanque(Integer idPalanque) {
        this.idPalanque = idPalanque;
    }

    public String getSite() {
        return site;
    }

    public void setSite(String site) {
        this.site = site;
    }

    public String getMeteo() {
        return meteo;
    }

    public void setMeteo(String meteo) {
        this.meteo = meteo;
    }

    public TypePlongee getTypePlongee() {
        return typePlongee;
    }

    public void setTypePlongee(TypePlongee typePlongee) {
        this.typePlongee = typePlongee;
    }

    public TypeInstrument getTypeInstrument() {
        return typeInstrument;
    }

    public void setTypeInstrument(TypeInstrument typeInstrument) {
        this.typeInstrument = typeInstrument;
    }
    
}
