package com.asptt.resabackend.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class Plongee implements Serializable {

    private static final long serialVersionUID = -2548032640337298221L;
    private Integer id;
    private TypePlongee typePlongee;
    private NiveauAutonomie niveauMinimum;
    private int nbMaxPlaces;
    private String niveauDP;
    private String lieux;
    private String warning;
    private Date datePlongee;
    private Date dateReservation;
    private Boolean ouvertureForcee;
    private String dp;
    private String pilote;
    private List<String> participants;
    private List<String> participantsEnAttente;

    public Plongee() {
        niveauMinimum = NiveauAutonomie.BATM;
        ouvertureForcee = true;
        nbMaxPlaces = 20;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public TypePlongee getTypePlongee() {
        return typePlongee;
    }

    public void setTypePlongee(TypePlongee typePlongee) {
        this.typePlongee = typePlongee;
    }

    public NiveauAutonomie getEnumNiveauMinimum() {
        return niveauMinimum;
    }

    public String getNiveauMinimum() {
        if (null == niveauMinimum) {
            return NiveauAutonomie.P0.toString();
        } else {
            return niveauMinimum.toString();
        }
    }

    public void setEnumNiveauMinimum(NiveauAutonomie niveauMinimum) {
        this.niveauMinimum = niveauMinimum;
    }

    public void setNiveauMinimum(String niveau) {
        if (null == niveau){
            this.niveauMinimum = NiveauAutonomie.BATM;
        } else {
            this.niveauMinimum = NiveauAutonomie.valueOf(niveau);
        }
    }

    public int getNbMaxPlaces() {
        return nbMaxPlaces;
    }

    public String getMaxPlaces() {
        return String.valueOf(nbMaxPlaces);
    }

    public void setNbMaxPlaces(int nbPlaces) {
        this.nbMaxPlaces = nbPlaces;
    }

    public void setMaxPlaces(String nbPlaces) {
        this.nbMaxPlaces = Integer.valueOf(nbPlaces);
    }

    public String getNiveauDP() {
        return niveauDP;
    }

    public void setNiveauDP(String niveauDP) {
        this.niveauDP = niveauDP;
    }

    public String getLieux() {
        return lieux;
    }

    public void setLieux(String lieux) {
        this.lieux = lieux;
    }

    public Date getDatePlongee() {
        return datePlongee;
    }

    public void setDatePlongee(Date datePlongee) {
        this.datePlongee = datePlongee;
    }

    public Date getDateReservation() {
        return dateReservation;
    }

    public void setDateReservation(Date date) {
        this.dateReservation = date;
    }

    public Boolean getOuvertureForcee() {
        return ouvertureForcee;
    }

    public void setOuvertureForcee(Boolean ouvertureForcee) {
        this.ouvertureForcee = ouvertureForcee;
    }

    public String getDp() {
        return dp;
    }

//    public String setDp() {
//        List<Adherent> participants = getParticipants();
//        List<Adherent> lesDPs = new ArrayList<>();
//        for (String adherent : participants) {
//            if (adherent.isDp()) {
//                lesDPs.add(adherent);
//            }
//        }
//        Collections.sort(lesDPs, new AdherentComparatorDP());
//        if (lesDPs.size() > 0) {
//            this.dp = lesDPs.get(0);
//        }
//        return dp;
//    }

    public void setDp(String dp) {
        this.dp = dp;
    }

    public String getPilote() {
        return pilote;
    }

    public void setPilote(String pilote) {
        this.pilote = pilote;
    }

    public List<String> getParticipants() {
        if (null == participants) {
            this.participants= new ArrayList<>();
        }
        return participants;
    }

    public void setParticipants(List<String> participants) {
        this.participants = participants;
    }

    public List<String> getParticipantsEnAttente() {
        if (null == participantsEnAttente) {
            this.participantsEnAttente = new ArrayList<>();
        }
        return participantsEnAttente;
    }

    public void setParticipantsEnAttente(List<String> participantsEnAttente) {
        this.participantsEnAttente = participantsEnAttente;
    }

    public boolean isExistDP() {
        if (null == getDp() || getDp().isEmpty()) {
            return false;
        } else {
            return true;
        }
    }

    public boolean isExistPilote() {
        if (null == getPilote() || getPilote().isEmpty()) {
            return false;
        } else {
            return true;
        }
    }

    public boolean isNbMiniAtteint(int nbMini) {
        if (getParticipants().size() >= nbMini) {
            return true;
        } else {
            return false;
        }
    }

    public String getWarning() {
        return warning;
    }

    public void setWarning(String warning) {
        this.warning = warning;
    }
    
}
