package com.asptt.resabackend.business;

import java.util.List;

import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;

import com.asptt.resa.commons.exception.Functional;
import com.asptt.resa.commons.exception.FunctionalException;
import com.asptt.resabackend.entity.Adherent;
import com.asptt.resabackend.entity.NiveauAutonomie;
import com.asptt.resabackend.entity.TypeEncadrement;
import com.asptt.resabackend.resources.adherent.AdherentService;
import com.asptt.resabackend.resources.plongee.PlongeeService;

public class ResaBusinessRGUtil {
	
	
	private static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(ResaBusinessImpl.class);


	@Autowired
	private Environment env;

	@Autowired
	private AdherentService serviceAdherent;

	@Autowired
	private PlongeeService servicePlongee;
	

	public void checkHeureOuverture(long nbJours, int heureCourante, int heureOuverture) {
		if (nbJours > 0) {
			// La date de visibilité de la plongée n'est pas atteinte : on attend
			throw new FunctionalException(Functional.REGISTRATION_HEURE_OUVERTURE);
//			throw new FunctionalException(Functional.REGISTRATION_HEURE_OUVERTURE,MessageFormat.format(ResaBackendMessage.ATTENDRE_HEURE_OUVERT, arguments
//					, heureOuverture));
		} else {
			if (nbJours == 0) {
				// C'est le jour de visibilité de la plongée : On regarde l'heure avant de
				// donner accès à l'inscription
				if (heureCourante < heureOuverture) {
					// Trop tot : attendre l'heure d'ouverture
					throw new FunctionalException(Functional.REGISTRATION_JOUR_HO);
//					throw new FunctionalException(Functional.GENERIC,
//							CatalogueMessages.INSCRIPTION_ATTENDRE_J_HO + "." + heureOuverture);
				}
			}
		}
	}
	
	protected void manageFunctionalErreurHeureOuverture(Integer code) {
		switch (code) {
		case 3:
			
			break;
		case 4:
			
			break;

		default:
			break;
		}

	}

	protected Integer getNbPlaceRestante(int nbMaxPlaces, Integer plongeeId) {
		Integer nbPlace = nbMaxPlaces - servicePlongee.getAdherentsInscrits(plongeeId, null, null, null).size();
		return nbPlace;
	}

	protected boolean isEnoughEncadrant(Integer plongeeId) {
		boolean isOk = true;
		List<String> encadrants = servicePlongee.getAdherentsInscrits(plongeeId, null, "TOUS", null);
		float nbEncadrant = encadrants.size();

		if (nbEncadrant <= 1) {
			// C'est le dernier encadrant => MAIL
			isOk = false;
			return isOk;
		} else {
			// il en reste au moins 1...
			List<String> plongeursP0 = servicePlongee.getAdherentsInscrits(plongeeId, "P0", null, null);
			int nbP0 = plongeursP0.size();
			List<String> plongeursP1 = servicePlongee.getAdherentsInscrits(plongeeId, "P1", null, null);
			int nbP1 = plongeursP1.size();
			List<String> plongeursBATM = servicePlongee.getAdherentsInscrits(plongeeId, "BATM", null, null);
			int nbBATM = plongeursBATM.size();

			// cas pour les P0, P1
			if (nbP0 + nbP1 > 0) {
				float res = ((nbP0 + nbP1)) / (nbEncadrant - 1);
				// max 4 P0 ou P1 par encadrant
				if (res > 4) {
					// Pas assez d'encadrant : envoie d'un mail
					isOk = false;
					return isOk;
				}
			}
			// cas pour les BATM
			if (nbBATM > 0) {
				float res = (nbBATM) / (nbEncadrant - 1);
				// max 1 bapteme par encadrant
				if (res > 1) {
					// Pas assez d'encadrant : liste d'attente
					isOk = false;
					return isOk;
				}
			}
			return isOk;
		}
	}
	
    public static boolean isDp(Adherent adh) {
		if (null == adh.getEncadrement() || null == adh.getNiveau() || null == adh ) {
			return false;
		}
        if (adh.getNiveau().equals(NiveauAutonomie.P5)) {
            return true;
        } else {
            if (adh.getEncadrement() != null) {
                if (adh.getEncadrement().equals(TypeEncadrement.E3)
                        || adh.getEncadrement().equals(TypeEncadrement.E4)) {
                    return true;
                }
            }
            return false;
        }
    }

	/**
	 * Une veste rouge peut-être : Un encadrant, un DP ou u Pilote Mais pas un
	 * externe => le test : et actif =1
	 * @return
	 */
	public static boolean isVesteRouge(Adherent adh) {
		boolean vesteRouge = false;
		if (adh.getActif() == 1) {
			if (adh.getEncadrement() != null) {
				vesteRouge=true;
			}
			if (adh.isDp()) {
				vesteRouge=true;
			}
			if (adh.isPilote()) {
				vesteRouge=true;
			}
		}
		// return ( (getEncadrement() != null || isDp() || isPilote() ) && getActifInt()
		// == 1);
		return vesteRouge;
	}

	public static String getNomComplet(Adherent adh) {
		// Dès que le plongeur est encadrant, on affiche son niveau d'encadrement
		String niveauAffiche;
		if (adh.getEncadrement() != null) {
			niveauAffiche = adh.getEncadrement().name();
		} else {
			niveauAffiche = adh.getNiveau().name();
		}

		// Pour les externes, le niveau est suffixé par (Ext.)
		if (adh.getActif() == 2) {
			niveauAffiche = niveauAffiche + " (Ext.)";
		}

		return adh.getNom() + " " + adh.getPrenom() + " " + niveauAffiche + " (" + adh.getTelephone() + ")";
	}

	public static String getNomCompletFS(Adherent adh) {
		// Pour les externes, ne nom est suffixé par (Ext.)
		String ext = "";
		if (adh.getActif() == 2) {
			ext = ext + " (Ext.)";
		}
		return adh.getNom() + " " + adh.getPrenom() + " " + ext;
	}


	public static boolean isActif(Adherent adh) {
		if(adh.getActif() == 1) {
			return true;
		} else {
			return false;
		}
	}

	public static String getPrerogative(Adherent adh) {
//		if (adh.isEncadrent()) {
		if (null != adh.getEncadrement()) {
			return adh.getEncadrement().name();
		} else {
			// petite verrue du 19/05/2011 demandée exclusivement pour notre ami Bernard
			// SANGUEDOLCE
			if (adh.getNumeroLicense().equalsIgnoreCase("095821")) {
				return "E1";
			} else {
				return adh.getNiveau().name();
			}
		}
	}


}
