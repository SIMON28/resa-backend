package com.asptt.resabackend.resources.adherent;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.UriInfo;

import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.asptt.resa.commons.exception.Functional;
import com.asptt.resa.commons.exception.FunctionalException;
import com.asptt.resa.commons.exception.NotFoundException;
import com.asptt.resa.commons.exception.TechnicalException;
import com.asptt.resa.commons.service.ServiceBase;
import com.asptt.resabackend.entity.Adherent;
import com.asptt.resabackend.entity.Adherent.Roles;
import com.asptt.resabackend.entity.ContactUrgent;
import com.asptt.resabackend.entity.Plongee;
import com.asptt.resabackend.resources.contacturgent.ContactService;
import com.asptt.resabackend.resources.plongee.PlongeeService;

@Service("adherentService")
public class AdherentServiceImpl extends ServiceBase<Adherent> implements AdherentService {

	private static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(AdherentServiceImpl.class);

	@Autowired
	private Environment env;

	@Autowired
	private AdherentDao adherentDao;

	@Autowired
	private ContactService contactService;

	@Autowired
	private PlongeeService plongeeService;

	@Override
	protected AdherentDao getDao() {
		return this.adherentDao;
	}

	@Override
	public String getId(final Adherent resource) {
		return resource.getNumeroLicense();
	}

	@Override
	public void setId(Adherent resource) {
		// TODO Auto-generated method stub

	}

	/**
	 * Description recherche un adherent avec son numero de license
	 * 
	 * @param String
	 *            id : numero de license de l'adherent
	 * @return Adherent
	 */
	@Override
	public Adherent get(String id) {
		return super.get(id);
	}

	@Override
	public Adherent create(Adherent resource) {
		try {
			this.get(resource.getNumeroLicense());
		} catch (NotFoundException e) {
			return super.create(resource);
		}
		throw new FunctionalException(Functional.REGISTRATION,
				"Creation Impossible, l'adherent avec le numero de license [" + resource.getNumeroLicense()
						+ "] existe déjà");
	}

	
	@Override
	public Integer findCount(MultivaluedMap<String, String> criteria) {
		return getDao().findCount(criteria);
	}
	
	@Override
	public List<Adherent> find(MultivaluedMap<String, String> criteria) {
//		for (Map.Entry<String, List<String>> entry : criteria.entrySet()) {
//			LOGGER.info("clé=[" + entry.getKey() + "] valeur=[" + entry.getValue().get(0) + "]");
//		}

		return super.find(criteria);
	}

	@Override
	@Transactional(rollbackFor = { TechnicalException.class })
	public Adherent update(String id, Adherent resource) {
		Adherent adhRollBack = this.get(id);
		adhRollBack.getRoles();
		if (resource.getNumeroLicense() != null) {
			if (!resource.getNumeroLicense().isEmpty()) {
				throw new FunctionalException(Functional.GENERIC,
						"Le Body ne doit pas contenir de numero de license : [" + resource.getNumeroLicense() + "]");
			}
		} else {
			// Gestion des roles
			for (Roles role : resource.getRoles()) {
				if (!adhRollBack.getRoles().contains(role)) {
					getDao().createRoleForAdherent(id, role);
				}
			}
			for (Roles role : adhRollBack.getRoles()) {
				if (!resource.getRoles().contains(role)) {
					getDao().deleteRoleForAdherent(id, role);
				}
			}
			// Gestion des contacts
			// 1er temps :vérification de l'existance des contacts à mettre à jour
			for (String contactId : resource.getContacts()) {
				contactService.get(contactId);
			}
			for (String contactId : resource.getContacts()) {
				if (!adhRollBack.getContacts().contains(contactId)) {
					// Nouveau contact : Ajouter le contactId dans la table de relation
					getDao().createContactForAdherent(id, contactId);
				}
			}
			for (String contactId : adhRollBack.getContacts()) {
				if (!resource.getContacts().contains(contactId)) {
					getDao().deleteContactForAdherent(id, contactId);
				}
			}
			resource.setNumeroLicense(id);
		}
		return super.update(resource);
	}

	@Override
	public List<ContactUrgent> findContacts(String adherentId) {
		return contactService.findContactsForAdherent(adherentId);
	}

	@Override
	public List<ContactUrgent> createContacts(String adherentId, List<ContactUrgent> resources) {
		Adherent adh = this.get(adherentId);
		return contactService.createContactsForAdherent(resources, adherentId);
	}

	@Override
	public void deleteContacts(String adherentId) {

		contactService.deleteContactsForAdherent(adherentId);
	}

	@Override
	public List<Plongee> findPlongees(UriInfo uriInfo, String adherentId) {
		Adherent adh = get(adherentId);
		List<Plongee> plongees = new ArrayList<>();
		if (adh.isVesteRouge()) {
			return plongees = plongeeService.findPlongeeForEncadrant(env.getProperty("reservation.max"),
					env.getProperty("visible.apres.encadrant"));
		} else {
			plongees = plongeeService.findPlongeeForAdherent();
			List<Plongee> plongeesOuvertes = new ArrayList<>();
			for (Plongee plongee : plongees) {
				if (plongee.isExistDP() && plongee.isExistPilote()) {
					plongeesOuvertes.add(plongee);
				}
			}
			return plongeesOuvertes;
		}
	}

}
