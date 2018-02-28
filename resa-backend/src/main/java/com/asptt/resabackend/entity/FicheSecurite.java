package com.asptt.resabackend.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.slf4j.LoggerFactory;

import com.asptt.resabackend.resources.adherent.AdherentDaoImpl;

public class FicheSecurite implements Serializable {

    private static final long serialVersionUID = -8569233748603210161L;
    private Integer id;
    private int idPlongee;
    private int statut;
    private String site;
    private String meteo;
    private String nomDP;
    private String nomPilote;
    private TypePlongee typePlongee;
    private List<Palanque> palanques;
    private int numeroPlongee;
    private int nbHeuresBateau;
    private Integer hhDepart;
    private Integer mnDepart;
    private String heureDepart;
    private String mer;
    private int nbPlongeurs;
    private int nbEncadrants;
    private Date datePlongee;

	private static final org.slf4j.Logger LOGGER = LoggerFactory
			.getLogger(AdherentDaoImpl.class);

    public FicheSecurite() {
    }

    public FicheSecurite(Plongee unePlongee) {
        //init de la plongée
//        this.plongee = unePlongee;
        this.idPlongee = unePlongee.getId();
        if (null == unePlongee.getDp()) {
            this.nomDP = "aucun";
        } else {
//            this.nomDP = unePlongee.getDp().getNom();
        }
        if (null == unePlongee.getPilote()) {
            this.nomPilote = "aucun";
        } else {
//            this.nomPilote = unePlongee.getPilote().getNom();
        }
        this.datePlongee = unePlongee.getDatePlongee();
        this.typePlongee = unePlongee.getTypePlongee();
        this.nbPlongeurs = unePlongee.getParticipants().size();
        List<Adherent> encadrants = new ArrayList<>();
//        for (Adherent plongeur : unePlongee.getParticipants()) {
//            if (plongeur.isEncadrent()) {
//                encadrants.add(plongeur);
//            }
//        }
        this.nbEncadrants = encadrants.size();
    }

    public int getStatut() {
        return statut;
    }

    public void setStatut(int statut) {
        this.statut = statut;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public int getIdPlongee() {
        return idPlongee;
    }

    public void setIdPlongee(int idPlongee) {
        this.idPlongee = idPlongee;
    }

    public String getNomDP() {
        if (null == nomDP || nomDP.isEmpty()) {
//            return plongee.getDp().getNom();
            return "nomDP?";
        } else {
            return nomDP;
        }
    }

    public void setNomDP(String nomDP) {
        this.nomDP = nomDP;
    }

    public String getNomPilote() {
        if (null == nomPilote || nomPilote.isEmpty()) {
//            return plongee.getPilote().getNom();
            return "nomPilote?";
        } else {
            return nomPilote;
        }
    }

    public void setNomPilote(String nomPilote) {
        this.nomPilote = nomPilote;
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

    public Date getDatePlongee() {
        if (null == datePlongee) {
            datePlongee = new Date();
        }
        return datePlongee;
    }

    public void setDatePlongee(Date datePlongee) {
        this.datePlongee = datePlongee;
    }

    public List<Palanque> getPalanques() {
        if (null == palanques) {
            palanques = new ArrayList<>();
        }
        return palanques;
    }

    public void setPalanques(List<Palanque> palanques) {
        this.palanques = palanques;
    }

    public int getNbPlongeurs() {
        if(nbPlongeurs==0){
            List l_idPlongeur = new ArrayList<String>();
//                nbPlongeurs = nbPlongeurs + pal.getListPlongeursPalanque().size();
            /** le nbPlongeur n'est pas initialisé 
             * on a pas l'objet plongée donc on passe par les palanquées 
             * dans le cas de bapteme le guide peut apparaitre dans plusieurs palanquées
             * donc on ne l'ajoute qu'une fois
             */
            //
            for (Palanque pal : getPalanques()){
                if ( null != pal.getIdGuide()  && ! l_idPlongeur.contains(pal.getIdGuide())){
                    l_idPlongeur.add(pal.getIdGuide());
                }
                if ( null != pal.getIdPlongeur1()&& ! l_idPlongeur.contains(pal.getIdPlongeur1())){
                    l_idPlongeur.add(pal.getIdPlongeur1());
                }
                if ( null != pal.getIdPlongeur2()&& ! l_idPlongeur.contains(pal.getIdPlongeur2())){
                    l_idPlongeur.add(pal.getIdPlongeur2());
                }
                if ( null != pal.getIdPlongeur3()&& ! l_idPlongeur.contains(pal.getIdPlongeur3())){
                    l_idPlongeur.add(pal.getIdPlongeur3());
                }
                if ( null != pal.getIdPlongeur4()&& ! l_idPlongeur.contains(pal.getIdPlongeur4())){
                    l_idPlongeur.add(pal.getIdPlongeur4());
                }
            }
            nbPlongeurs = l_idPlongeur.size();
        }
        return nbPlongeurs;
    }

    public void setNbPlongeurs(int nbPlongeurs) {
        this.nbPlongeurs = nbPlongeurs;
    }

    public int getNbEncadrants() {
        return nbEncadrants;
    }

    public int getNbPalanques() {
        if (null == palanques) {
            return 0;
        } else {
            return palanques.size();
        }
    }

    public int getNumeroPlongee() {
        return numeroPlongee;
    }

    public void setNumeroPlongee(int numeroPlongee) {
        this.numeroPlongee = numeroPlongee;
    }

    public int getNbHeuresBateau() {
        return nbHeuresBateau;
    }

    public void setNbHeuresBateau(int nbHeuresBateau) {
        this.nbHeuresBateau = nbHeuresBateau;
    }

    public Integer getHhDepart() {
        if(null == hhDepart){
            hhDepart=0;
        }
        return hhDepart;
    }

    public void setHhDepart(int hhDepart) {
        this.hhDepart = hhDepart;
    }

    public Integer getMnDepart() {
        if(null == mnDepart){
            mnDepart=0;
        }
        return mnDepart;
    }

    public void setMnDepart(int mnDepart) {
        this.mnDepart = mnDepart;
    }

    public void setHhMnDepart(String value) {
        if (null == value || value.equals("") || value.equals("h")) {
            value = getHeureDepart();
        }
        String hh = value.substring(0, value.indexOf('h'));
        String mn = value.substring(value.indexOf('h') + 1);
        setHhDepart(Integer.parseInt(hh));
        setMnDepart(Integer.parseInt(mn));
    }

    public void setHeureDepart(String hs) {
        heureDepart = hs;
    }

    public void setHeureDepart(int hour, int minute) {
        String hh = "";
        String mn = "";
        if (hour < 10) {
            hh = "0" + hour;
        } else {
            hh = Integer.toString(hour);
        }
        if (minute < 10) {
            mn = "0" + minute;
        } else {
            mn = Integer.toString(minute);
        }
        String sb = hh + "h" + mn;
        heureDepart = sb;
    }

    public String getHeureDepart() {
        if (null == heureDepart || heureDepart.equals("h")) {
            heureDepart = "00h00";
        }
        return heureDepart;
    }

    public String getMer() {
        return mer;
    }

    public void setMer(String mer) {
        this.mer = mer;
    }

}
