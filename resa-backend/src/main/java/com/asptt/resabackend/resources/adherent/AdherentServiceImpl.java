package com.asptt.resabackend.resources.adherent;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.UriInfo;

import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.asptt.resa.commons.exception.BadUsage;
import com.asptt.resa.commons.exception.BadUsageException;
import com.asptt.resa.commons.exception.Functional;
import com.asptt.resa.commons.exception.FunctionalException;
import com.asptt.resa.commons.exception.NotFoundException;
import com.asptt.resa.commons.exception.TechnicalException;
import com.asptt.resa.commons.service.ServiceBase;
import com.asptt.resa.commons.utils.URIParserUtils;
import com.asptt.resabackend.entity.Adherent;
import com.asptt.resabackend.entity.Adherent.Roles;
import com.asptt.resabackend.entity.ContactUrgent;
import com.asptt.resabackend.entity.Plongee;
import com.asptt.resabackend.entity.TypeActionReturnPlongee;
import com.asptt.resabackend.resources.contacturgent.ContactService;
import com.asptt.resabackend.resources.plongee.PlongeeService;
import com.asptt.resabackend.util.ResaUtil;


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
	@Transactional(rollbackFor = { TechnicalException.class })
	public Adherent create(Adherent resource) {
		try {
			this.get(resource.getNumeroLicense());
		} catch (NotFoundException e) {
			return super.create(resource);
		}
		throw new FunctionalException(Functional.REGISTRATION,MessageFormat.format(
				"Creation Impossible, l''adherent avec le numero de license [{0}] existe déjà",resource.getNumeroLicense()));
	}

	@Override
	public Integer findCount(MultivaluedMap<String, String> criteria) {
		return getDao().findCount(criteria);
	}

	@Override
	public List<Adherent> find(MultivaluedMap<String, String> criteria) {
		// for (Map.Entry<String, List<String>> entry : criteria.entrySet()) {
		// LOGGER.info("clé=[" + entry.getKey() + "] valeur=[" + entry.getValue().get(0)
		// + "]");
		// }

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
	@Transactional
	public List<ContactUrgent> createContacts(String adherentId, List<ContactUrgent> resources) {
		// verifie si cet adherent existe bien.
		Adherent adh = this.get(adherentId);
		return contactService.createContactsForAdherent(resources, adherentId);
	}

	@Override
	@Transactional
	public void deleteContacts(String adherentId) {
		contactService.deleteContactsForAdherent(adherentId);
	}

	@Override
	public List<Plongee> findPlongees(UriInfo uriInfo, String adherentId) {
		List<Plongee> result = new ArrayList<>();
		MultivaluedMap<String, String> criterias = URIParserUtils.extractCriteria(uriInfo.getQueryParameters());
		if (criterias.containsKey("action")) {
			String actionValue = criterias.getFirst("action");
			TypeActionReturnPlongee value = TypeActionReturnPlongee.fromString(actionValue);
			if (null != value) {
				Adherent adherent = get(adherentId);
				switch (value) {
				case CONSULTER:
					if (adherent.isVesteRouge()) {
						result = plongeeService.findPlongeeForEncadrant(env.getProperty("reservation.max"),
								env.getProperty("visible.apres.encadrant"));
					} else {
						List<Plongee> plongees = plongeeService.findPlongeeForAdherent(TypeActionReturnPlongee.CONSULTER);
						// recherche les plongées ouvertes càd ayant un DP et un Pilote
						for (Plongee plongee : plongees) {
							if (plongee.isExistDP() && plongee.isExistPilote()) {
								result.add(plongee);
							}
						}
					}
					break;
				case RESERVER:
					List<Plongee> plongees = new ArrayList<>();
					if (adherent.isVesteRouge()) {
						plongees = plongeeService.findPlongeeForEncadrant(env.getProperty("reservation.max"),
								env.getProperty("visible.apres.encadrant"));
					} else {
						plongees = plongeeService.findPlongeeForAdherent(TypeActionReturnPlongee.RESERVER);
						// recherche les plongées ouvertes càd ayant un DP et un Pilote
					}
					for (Plongee plongee : plongees) {
			            boolean isNotInscrit = true;
			            for (String pId : plongee.getParticipants()) {
			                if (pId.equalsIgnoreCase(adherent.getNumeroLicense())) {
			                    isNotInscrit = false;
			                    break;
			                }
			            }
			            for (String paId : plongee.getParticipantsEnAttente()) {
			                if (paId.equalsIgnoreCase(adherent.getNumeroLicense())) {
			                    isNotInscrit = false;
			                    break;
			                }
			            }
			            if (isNotInscrit) {
			                long nbJours = ResaUtil.checkDateCM(adherent.getDateCM(), plongee.getDatePlongee());
			                if (nbJours >= 0) {
			                    result.add(plongee);
			                }
			            }
			        }
					break;
				case DESINSCRIRE:
					// TODO findDesincription
					break;
				}
			} else {
				throw new BadUsageException(BadUsage.UNKNOWN_VALUE,
						"Parameter 'action' must be 'consulter, 'reserver' or 'desinscrire'");
			}

		} else {
			throw new BadUsageException(BadUsage.MISSING_QUERY, "Query parameter 'action' is mandatory");
		}
		return result;
	}

}
