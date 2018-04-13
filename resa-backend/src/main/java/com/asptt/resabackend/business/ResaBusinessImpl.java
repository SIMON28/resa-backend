package com.asptt.resabackend.business;

import java.text.MessageFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import javax.ws.rs.core.UriInfo;

import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import com.asptt.resa.commons.exception.BadUsage;
import com.asptt.resa.commons.exception.BadUsageException;
import com.asptt.resa.commons.exception.Functional;
import com.asptt.resa.commons.exception.FunctionalException;
import com.asptt.resabackend.entity.Adherent;
import com.asptt.resabackend.entity.Adherent.Encadrement;
import com.asptt.resabackend.entity.NiveauAutonomie;
import com.asptt.resabackend.entity.Plongee;
import com.asptt.resabackend.resources.adherent.AdherentService;
import com.asptt.resabackend.resources.plongee.PlongeeService;
import com.asptt.resabackend.util.CatalogueMessages;
import com.asptt.resabackend.util.ResaBackendMessage;
import com.asptt.resabackend.util.ResaUtil;

@Service
public class ResaBusinessImpl implements ResaBusiness {

	private static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(ResaBusinessImpl.class);

	@Autowired
	private Environment env;

	@Autowired
	private AdherentService serviceAdherent;

	@Autowired
	private PlongeeService servicePlongee;
	
	@Override
	public void checkOrderForDive(UriInfo uriInfo, Integer plongeeId,  OrderForDive order) {
		servicePlongee.get(plongeeId.toString());
		//check order mandatory fields action, adherentId
		if(null==order.getAction() ) {
			throw new BadUsageException(BadUsage.MANDATORY_FIELDS,
					"order.action is mandatory");
		}
		if(null==order.getAdherentId()) {
			throw new BadUsageException(BadUsage.MANDATORY_FIELDS,
					"order.adherentId is mandatory");
		} else {
			serviceAdherent.get(order.getAdherentId());
		}
		switch(order.getAction()) {
		case ADD:
			isOkForResa(plongeeId, order.getAdherentId());
			break;
		case DELETE:
			break;
		case WAITE:
			break;
		}
		
	}

	@Override
	public void isOkForResa(Integer plongeeId, String adherentId) {
		LOGGER.info("Recup environnemt 'nb.plongeur.mini'[" + env.getProperty("nb.plongeur.mini") + "]");
		Plongee plongee = servicePlongee.get(plongeeId.toString());
		Adherent adherent = serviceAdherent.get(adherentId);

		// initialisation du retour par defaut à 1 CàD : on inscrit
		// int isOk = 1;

		// Init de l'heure courante
		Date dateDuJour = new Date();
		Calendar calDuJour = Calendar.getInstance();
		calDuJour.setTime(dateDuJour);
		int heureCourante = calDuJour.get(Calendar.HOUR_OF_DAY);
		// Initialisation de l'heure d'ouverture
		int heureOuverture = getHeureOuverture(adherent, plongee);
		long nbJours = ResaUtil.calculNbJour(dateDuJour, plongee.getDateReservation());
//		long nbJours = 0;
//		int heureCourante = 10;
//		int heureOuverture = 12;
		// Test si l'adherent est déjà inscrit à la plongée
		for (String participantId : plongee.getParticipants()) {
			if (participantId.equalsIgnoreCase(adherent.getNumeroLicense())) {
				throw new FunctionalException(Functional.GENERIC, MessageFormat.format
						(ResaBackendMessage.REGISTRATION_OKFORRESA_ADH_INSCRIT, adherent.getPrenom()) );
			}
		}

		// Verification de l'heure d'ouverture a la reservation
		// Si l'adherent est actif et non encadrant  ou  si c'est un externe
		if ((!adherent.isVesteRouge() && adherent.getActifInt() == 1) || (adherent.getActifInt() == 2)) {
			try {
				checkHeureOuverture(nbJours, heureCourante, heureOuverture);
			} catch (FunctionalException e) {
				switch (e.getCode().getCode()) {
				case 3:
					throw new FunctionalException(Functional.REGISTRATION_HEURE_OUVERTURE,MessageFormat.format(
							ResaBackendMessage.REGISTRATION_ATTENDRE_HEURE_OUVERT, 
							adherent.getPrenom(),
							ResaUtil.getDateString(plongee.getDateReservation()),
							heureOuverture));

				case 4:
					throw new FunctionalException(Functional.REGISTRATION_JOUR_HO,MessageFormat.format(
							ResaBackendMessage.REGISTRATION_ATTENDRE_J_HO, 
							adherent.getPrenom(),
							heureOuverture));
				default:
					throw new FunctionalException(Functional.REGISTRATION,ResaBackendMessage.REGISTRATION_GENERIC+" Erreur : "+e.getMessage());
				}
			}
		}
		// si c'est un externe
//		if (adherent.getActifInt() == 2) {
//			checkHeureOuverture(nbJours, heureCourante, heureOuverture);
//		}
		
		// Inscription impossible -blocage- il y a juste le nombre pour ouvrir la plongée.
		// pour les plongées avec dateDePlongee avant 12h00 blocage à 23h00 la veille,
		// après 12h00 blocage le matin 8h00
		if (!plongee.isNbMiniAtteint(new Integer(env.getProperty("nb.plongeur.mini")).intValue())) {
			GregorianCalendar gcPlongee = new GregorianCalendar();
			gcPlongee.setTime(plongee.getDatePlongee());
			GregorianCalendar gcBlocage = new GregorianCalendar();
			gcBlocage.setTime(plongee.getDatePlongee());
			if (gcPlongee.get(GregorianCalendar.HOUR_OF_DAY) <= 12) {
				gcBlocage.add(GregorianCalendar.DATE, -1);
				gcBlocage.set(GregorianCalendar.HOUR_OF_DAY,
						new Integer(env.getProperty("heure.blocage.AM")).intValue());
				gcBlocage.set(GregorianCalendar.MINUTE, 0);
				gcBlocage.set(GregorianCalendar.SECOND, 0);
			} else {
				gcBlocage.set(GregorianCalendar.HOUR_OF_DAY,
						new Integer(env.getProperty("heure.blocage.PM")).intValue());
				gcBlocage.set(GregorianCalendar.MINUTE, 0);
				gcBlocage.set(GregorianCalendar.SECOND, 0);
			}

			if (calDuJour.getTime().after(gcBlocage.getTime())) {
				throw new FunctionalException(Functional.REGISTRATION_NB_MINI_PLONGEUR, MessageFormat.format(
						ResaBackendMessage.REGISTRATION_NB_MINI_PLONGEUR,
						adherent.getPrenom())
						);
				// throw new ResaException(CatalogueMessages.INSCRIPTION_IMPOSSIBLE + "." +
				// Parameters.getString("inscription.impossible"));
			}

		}
		// verifier le nombre d'inscrit
		if (getNbPlaceRestante(plongee.getNbMaxPlaces(), plongeeId) <= 0) {
			// TODO trop de monde : inscription en liste d'attente avec msg 'bateau plein'
			// isOk = 0;
			// return isOk;
			// TODO throw exception
		}

		// Si il y a des gens en liste d'attente on empile dans la liste d'attente
		if (plongee.getParticipantsEnAttente().size() > 0) {
			// TODO il y a des gens en liste d'attente,
			// isOk = 5;
			// return isOk;
			// TODO throw exception
		}

		// SI encadrant veux reserver une plongée pas encore ouverte:
		// si DP + Pilote > le brancher sur ouvrirplongee
		// sinon > impossible
		if (!plongeeIsOuverte(plongee)) {
			if (adherent.isDp() && adherent.isPilote()) {
				// TODO créer une opération 'order' pour ouvrir une plongée par DP+Pilote encore fermée
				// isOk = 2;
			} else if (adherent.isVesteRouge()) {
				// TODO inscription d'office à la plongée
				// Encadrant ou DP ou Pilote : il peut s'inscrire à la plongée même si elle est fermée
				// isOk = 3;
			} else {
				// pas de assez de compétences pour ouvrir la plongée : pas inscrit !
				// throw new ResaException(CatalogueMessages.INSCRIPTION_KO_PLONGEE_FERMEE);
				throw new FunctionalException(Functional.REGISTRATION_PLONGEE_FERMEE, MessageFormat.format(
						ResaBackendMessage.REGISTRATION_PLONGEE_FERMEE,
						ResaUtil.getHeurePlongee(plongee.getDatePlongee()), 
						ResaUtil.getDateString(plongee.getDateReservation())));
			}
			// return isOk;
			// TODO ??? Verifier mais normalement on ne doit jamais passer par là?... throw exception
		}

		// Si DP = P5 et pas encadrant (plus que E2) => pas de BATM ou de P0
		// recuperation du plongeur DP
		Adherent dp = serviceAdherent.get(plongee.getDp());
		String niveauDP = "";
		if (dp.getEncadrement() != null && !dp.getEncadrement().equals(Encadrement.E2)) {
			niveauDP = dp.getEncadrement();
		} else {
			niveauDP = dp.getNiveau();
		}
		if (niveauDP.equalsIgnoreCase("P5")) {
			if (adherent.getNiveau().equalsIgnoreCase(NiveauAutonomie.BATM.toString())
					|| adherent.getNiveau().equalsIgnoreCase(NiveauAutonomie.P0.toString())) {
				// inscription refusée
				// throw new ResaException(CatalogueMessages.INSCRIPTION_KO_DP_P5);
				throw new FunctionalException(Functional.GENERIC, CatalogueMessages.INSCRIPTION_KO_DP_P5);
			}
		}

		// verifier le niveau mini
		int niveauAdherent = -1;
		if (!adherent.getNiveau().equalsIgnoreCase(NiveauAutonomie.BATM.toString())) {
			niveauAdherent = new Integer(adherent.getNiveau().substring(1));
		}

		// verifier le niveau mini de la plongée
		int niveauMinPlongee = -1;
		if (!plongee.getEnumNiveauMinimum().equals(NiveauAutonomie.BATM)) {
			niveauMinPlongee = new Integer(plongee.getNiveauMinimum().toString().substring(1));
		}
		if (niveauAdherent < niveauMinPlongee) {
			// niveau mini requis : inscription refusée
			throw new FunctionalException(Functional.GENERIC, CatalogueMessages.INSCRIPTION_KO_NIVEAU_MINI);
			// throw new ResaException(CatalogueMessages.INSCRIPTION_KO_NIVEAU_MINI);
		}

		// On inscrit pas qqlun si il est dejà en liste d'attente
		List<String> enAttente = servicePlongee.getAdherentsWaiting(plongeeId);
		for (String attenteId : enAttente) {
			if (attenteId.equalsIgnoreCase(adherent.getNumeroLicense())) {
				throw new FunctionalException(Functional.GENERIC, CatalogueMessages.INSCRIPTION_KO_DEJA_EN_ATTENTE);
				// throw new ResaException(CatalogueMessages.INSCRIPTION_KO_DEJA_EN_ATTENTE);
			}
		}

		List<String> encadrants = servicePlongee.getAdherentsInscrits(plongeeId, null, "TOUS", null);
		float nbEncadrant = encadrants.size();
		List<String> plongeursP0 = servicePlongee.getAdherentsInscrits(plongeeId, "P0", null, null);
		int nbP0 = plongeursP0.size();
		List<String> plongeursP1 = servicePlongee.getAdherentsInscrits(plongeeId, "P1", null, null);
		int nbP1 = plongeursP1.size();
		List<String> plongeursBATM = servicePlongee.getAdherentsInscrits(plongeeId, "BATM", null, null);
		int nbBATM = plongeursBATM.size();

		// Calcul du nombre d'encadrant
		// Pour les plongeurs P0, P1
		if (niveauAdherent >= 0 && niveauAdherent < 2) {
			float res = ((nbP0 + nbP1) + 1) / nbEncadrant;
			// max 4 P0 ou P1 par encadrant
			if (res > 4) {
				// Pas assez d'encadrant : liste d'attente avec envoi de mail
				// isOk = 4;
				// return isOk;
				// TODO throw exception
			}
		}
		// Pour les BATM
		if (niveauAdherent < 0) {
			float res = (nbBATM + 1) / nbEncadrant;
			// max 1 bapteme par encadrant
			if (res > 1) {
				// Pas assez d'encadrant : liste d'attente avec envoi de mail
				// isOk = 4;
				// return isOk;
				// TODO throw exception
			}
		}

		// Issue 69 : limiter le nombre d'encadrant à 7
		int nbEncadrantMax = new Integer(new Integer(env.getProperty("encadrant.max")).intValue());
		if (nbEncadrant >= nbEncadrantMax && adherent.isVesteRouge()) {
			// On créé un externe bidon pour passer a le methode de recherche de l'heure
			// d'ouvertutre
			// afin de ramener l'heure d'ouverture pour les externes
			Adherent ext = new Adherent();
			ext.setActifInt(2);
			int heureOuvertureExterne = getHeureOuverture(ext, plongee);
			if (nbJours > 0) {
				// La date de visibilité de la plongée n'est pas atteinte : on attend
				throw new FunctionalException(Functional.GENERIC, CatalogueMessages.INSCRIPTION_ATTENDRE_VR_HO);
				// throw new ResaException(CatalogueMessages.INSCRIPTION_ATTENDRE_VR_HO + "." +
				// heureOuvertureExterne);
			} else {
				if (nbJours == 0) {
					// C'est le jour de visibilité de la plongée : On regarde l'heure avant de
					// donner accès à l'inscription
					if (heureCourante < heureOuvertureExterne) {
						// Trop tot : attendre l'heure d'ouverture
						throw new FunctionalException(Functional.GENERIC,
								CatalogueMessages.INSCRIPTION_ATTENDRE_VR_J_HO);
						// throw new ResaException(
						// CatalogueMessages.INSCRIPTION_ATTENDRE_VR_J_HO + "." +
						// heureOuvertureExterne);
					}
				}
			}
			// Si le nombre de jour est < 0 : la date de visi est depassée donc on inscrit
		}

		// SI on est arrivé jusqu'ici : c'est bon => on inscrit
		// isOk = 1;
		// return isOk;
		// TODO throw exception

	}

	@Override
	public boolean plongeeIsOuverte(Plongee plongee) {
		if (plongee.isExistDP() && plongee.isExistPilote()) {
			return true;
		} else {
			return false;
		}
	}

	protected int getHeureOuverture(Adherent adh, Plongee plongee) {
		Calendar calOpen = Calendar.getInstance();
		calOpen.setTime(plongee.getDateReservation());
		int numJour = calOpen.get(Calendar.DAY_OF_WEEK);
		int heure;
		// //Calendar de la plongée pour rechercher le numero du jour de la plongée
		// Calendar calPlongee = Calendar.getInstance();
		// calPlongee.setTime(plongee.getDatePlongee());
		// int numPlongee = calPlongee.get(Calendar.DAY_OF_WEEK);
		switch (numJour) {
		case 1: // Dimanche
			// test actif == 1 ==> ok c un adherent
			// sinon c un externe donc inscription par secretatiat
			if (adh.getActifInt() == 1) {
				heure = new Integer(env.getProperty("ouverture.dimanche.adh")).intValue();
			} else {
				heure = new Integer(env.getProperty("ouverture.dimanche.sct")).intValue();
			}
			break;
		case 2: // Lundi
			if (adh.getActifInt() == 1) {
				heure = new Integer(env.getProperty("ouverture.lundi.adh")).intValue();
			} else {
				heure = new Integer(env.getProperty("ouverture.lundi.sct")).intValue();
			}
			break;
		case 3: // Mardi
			if (adh.getActifInt() == 1) {
				heure = new Integer(env.getProperty("ouverture.mardi.adh")).intValue();
			} else {
				heure = new Integer(env.getProperty("ouverture.mardi.sct")).intValue();
			}
			break;
		case 4: // Mercredi
			if (adh.getActifInt() == 1) {
				heure = new Integer(env.getProperty("ouverture.mercredi.adh")).intValue();
			} else {
				heure = new Integer(env.getProperty("ouverture.mercredi.sct")).intValue();
			}
			break;
		case 5: // Jeudi
			if (adh.getActifInt() == 1) {
				heure = new Integer(env.getProperty("ouverture.jeudi.adh")).intValue();
			} else {
				heure = new Integer(env.getProperty("ouverture.jeudi.sct")).intValue();
			}
			break;
		case 6: // Vendredi
			if (adh.getActifInt() == 1) {
				heure = new Integer(env.getProperty("ouverture.vendredi.adh")).intValue();
			} else {
				heure = new Integer(env.getProperty("ouverture.vendredi.sct")).intValue();
			}
			break;
		case 7: // Samedi
			if (adh.getActifInt() == 1) {
				heure = new Integer(env.getProperty("ouverture.samedi.adh")).intValue();
			} else {
				heure = new Integer(env.getProperty("ouverture.samedi.sct")).intValue();
			}
			break;
		default:
			heure = 0;
			break;
		}
		return heure;
	}

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

}
