/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.asptt.resabackend.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 *
 * @author ecus6396
 */
public class Palanque implements Serializable {

    private static final long serialVersionUID = -8569233748603210161L;
    private Integer id;
    private int numero;
    private Date datePlongee;
    private String idGuide;
    private String idPlongeur1;
    private String idPlongeur2;
    private String idPlongeur3;
    private String idPlongeur4;
    private int profondeurMaxPrevue;
    private int profondeurMaxRea;
    private int dureeTotalePrevue;
    private int dureeTotaleRea;
    private int palier3m;
    private int palier6m;
    private int palier9m;
    private int palierProfond;
    private int hhSortie;
    private int mnSortie;
    private String heureSortie;
    private String nomCompletGuide;
    private String nomCompletPlongeur1;
    private String nomCompletPlongeur2;
    private String nomCompletPlongeur3;
    private String nomCompletPlongeur4;
    private String aptGuide;
    private String aptPlongeur1;
    private String aptPlongeur2;
    private String aptPlongeur3;
    private String aptPlongeur4;
    private String aptitudePlongeur;
    private boolean isBapteme = false;

    public Palanque() {
    }

    public Palanque(Palanque palEntity) {
        this.id = palEntity.getId();
        this.numero = palEntity.getNumero();
        this.profondeurMaxPrevue = palEntity.getProfondeurMaxPrevue();
        this.profondeurMaxRea = palEntity.getProfondeurMaxRea();
        this.dureeTotalePrevue = palEntity.getDureeTotalePrevue();
        this.dureeTotaleRea = palEntity.getDureeTotaleRea();
        this.palier3m = palEntity.getPalier3m();
        this.palier6m = palEntity.getPalier6m();
        this.palier9m = palEntity.getPalier9m();
        this.palierProfond = palEntity.getPalierProfond();
        this.heureSortie = palEntity.getHeureSortie();
        this.idGuide = palEntity.getIdGuide();
        this.idPlongeur1 = palEntity.getIdPlongeur1();
        this.idPlongeur2 = palEntity.getIdPlongeur2();
        this.idPlongeur3 = palEntity.getIdPlongeur3();
        this.idPlongeur4 = palEntity.getIdPlongeur4();
        this.nomCompletGuide = palEntity.getNomCompletGuide();
        this.nomCompletPlongeur1 = palEntity.getNomCompletPlongeur1();
        this.nomCompletPlongeur2 = palEntity.getNomCompletPlongeur2();
        this.nomCompletPlongeur3 = palEntity.getNomCompletPlongeur3();
        this.nomCompletPlongeur4 = palEntity.getNomCompletPlongeur4();
        this.aptGuide = "";
        this.aptPlongeur1 = "";
        this.aptPlongeur2 = "";
        this.aptPlongeur3 = "";
        this.aptPlongeur4 = "";
        this.aptitudePlongeur = "";
        this.isBapteme = palEntity.getIsBapteme();
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public int getNumero() {
        return numero;
    }

    public void setNumero(int numero) {
        this.numero = numero;
    }

    public Date getDatePlongee() {
        return datePlongee;
    }

    public void setDatePlongee(Date datePlongee) {
        this.datePlongee = datePlongee;
    }

    public String getIdGuide() {
        return idGuide;
    }

    public void setIdGuide(String idGuide) {
        this.idGuide = idGuide;
    }

    public String getIdPlongeur1() {
        return idPlongeur1;
    }

    public void setIdPlongeur1(String idPlongeur1) {
        this.idPlongeur1 = idPlongeur1;
    }

    public String getIdPlongeur2() {
        return idPlongeur2;
    }

    public void setIdPlongeur2(String idPlongeur2) {
        this.idPlongeur2 = idPlongeur2;
    }

    public String getIdPlongeur3() {
        return idPlongeur3;
    }

    public void setIdPlongeur3(String idPlongeur3) {
        this.idPlongeur3 = idPlongeur3;
    }

    public String getIdPlongeur4() {
        return idPlongeur4;
    }

    public void setIdPlongeur4(String idPlongeur4) {
        this.idPlongeur4 = idPlongeur4;
    }

    public int getProfondeurMaxPrevue() {
        return profondeurMaxPrevue;
    }

    public void setProfondeurMaxPrevue(int profondeurMaxPrevue) {
        this.profondeurMaxPrevue = profondeurMaxPrevue;
    }

    public int getProfondeurMaxRea() {
        return profondeurMaxRea;
    }

    public void setProfondeurMaxRea(int profondeurMaxRea) {
        this.profondeurMaxRea = profondeurMaxRea;
    }

    public int getDureeTotalePrevue() {
        return dureeTotalePrevue;
    }

    public void setDureeTotalePrevue(int dureeTotalePrevue) {
        this.dureeTotalePrevue = dureeTotalePrevue;
    }

    public int getDureeTotaleRea() {
        return dureeTotaleRea;
    }

    public void setDureeTotaleRea(int dureeTotaleRea) {
        this.dureeTotaleRea = dureeTotaleRea;
    }

    public int getPalier3m() {
        return palier3m;
    }

    public void setPalier3m(int palier3m) {
        this.palier3m = palier3m;
    }

    public int getPalier6m() {
        return palier6m;
    }

    public void setPalier6m(int palier6m) {
        this.palier6m = palier6m;
    }

    public int getPalier9m() {
        return palier9m;
    }

    public void setPalier9m(int palier9m) {
        this.palier9m = palier9m;
    }

    public int getPalierProfond() {
        return palierProfond;
    }

    public void setPalierProfond(int palierProfond) {
        this.palierProfond = palierProfond;
    }

    public int getHhSortie() {
        return hhSortie;
    }

    public void setHhSortie(int hhSortie) {
        this.hhSortie = hhSortie;
    }

    public int getMnSortie() {
        return mnSortie;
    }

    public void setMnSortie(int mnSortie) {
        this.mnSortie = mnSortie;
    }

    public void setHhMnSortie(String  value) {
        if(null == value){
            value = getHeureSortie();
        }
        String hh = value.substring(0, value.indexOf('h'));
        String mn = value.substring(value.indexOf('h')+1);
        setHhSortie(new Integer(hh).intValue());
        setMnSortie(new Integer(mn).intValue());
    }

    public void setHeureSortie(String hs) {
        heureSortie =  hs;
    }

    public void setHeureSortie(int hour, int minute) {
        StringBuilder sb = new StringBuilder(hour+"h"+minute);
        heureSortie =  sb.toString();
    }

    public String getHeureSortie() {
        if(null == heureSortie){
            heureSortie="00h00";
        }
        return heureSortie;
    }

    public String getNomCompletGuide() {
        return nomCompletGuide;
    }

    public void setNomCompletGuide(String nomCompletGuide) {
        this.nomCompletGuide = nomCompletGuide;
    }

    public String getNomCompletPlongeur1() {
        return nomCompletPlongeur1;
    }

    public void setNomCompletPlongeur1(String nomCompletPlongeur1) {
        this.nomCompletPlongeur1 = nomCompletPlongeur1;
    }

    public String getNomCompletPlongeur2() {
        return nomCompletPlongeur2;
    }

    public void setNomCompletPlongeur2(String nomCompletPlongeur2) {
        this.nomCompletPlongeur2 = nomCompletPlongeur2;
    }

    public String getNomCompletPlongeur3() {
        return nomCompletPlongeur3;
    }

    public void setNomCompletPlongeur3(String nomCompletPlongeur3) {
        this.nomCompletPlongeur3 = nomCompletPlongeur3;
    }

    public String getNomCompletPlongeur4() {
        return nomCompletPlongeur4;
    }

    public void setNomCompletPlongeur4(String nomCompletPlongeur4) {
        this.nomCompletPlongeur4 = nomCompletPlongeur4;
    }

    public String getAptGuide() {
        return aptGuide;
    }

    public void setAptGuide(String aptGuide) {
        this.aptGuide = aptGuide;
    }

    public String getAptPlongeur1() {
        return aptPlongeur1;
    }

    public void setAptPlongeur1(String aptPlongeur1) {
        this.aptPlongeur1 = aptPlongeur1;
    }

    public String getAptPlongeur2() {
        return aptPlongeur2;
    }

    public void setAptPlongeur2(String aptPlongeur2) {
        this.aptPlongeur2 = aptPlongeur2;
    }

    public String getAptPlongeur3() {
        return aptPlongeur3;
    }

    public void setAptPlongeur3(String aptPlongeur3) {
        this.aptPlongeur3 = aptPlongeur3;
    }

    public String getAptPlongeur4() {
        return aptPlongeur4;
    }

    public void setAptPlongeur4(String aptPlongeur4) {
        this.aptPlongeur4 = aptPlongeur4;
    }

    public String getAptitudePlongeur() {
        return aptitudePlongeur;
    }

    public void setAptitudePlongeur(String aptitudePlongeur) {
        this.aptitudePlongeur = aptitudePlongeur;
    }

    public List<ChoiceRenderPlongeur> getListPlongeursPalanque() {
        List<ChoiceRenderPlongeur> listPlongeursPalanque = new ArrayList<>();
        if (null != getIdGuide()) {
            listPlongeursPalanque.add(new ChoiceRenderPlongeur(getIdGuide(), getNomCompletGuide()));
        }
        if (null != this.getIdPlongeur1()) {
            listPlongeursPalanque.add(new ChoiceRenderPlongeur(getIdPlongeur1(), getNomCompletPlongeur1()));
        }
        if (null != this.getIdPlongeur2()) {
            listPlongeursPalanque.add(new ChoiceRenderPlongeur(getIdPlongeur2(), getNomCompletPlongeur2()));
        }
        if (null != this.getIdPlongeur3()) {
            listPlongeursPalanque.add(new ChoiceRenderPlongeur(getIdPlongeur3(), getNomCompletPlongeur3()));
        }
        if (null != this.getIdPlongeur4()) {
            listPlongeursPalanque.add(new ChoiceRenderPlongeur(getIdPlongeur4(), getNomCompletPlongeur4()));
        }
        return listPlongeursPalanque;
    }

    public boolean getIsBapteme() {
        return isBapteme;
    }

    public void setIsBapteme(boolean isBapteme) {
        this.isBapteme = isBapteme;
    }

}
