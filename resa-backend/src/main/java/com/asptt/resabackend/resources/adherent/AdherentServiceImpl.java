package com.asptt.resabackend.resources.adherent;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.asptt.resa.commons.exception.Functional;
import com.asptt.resa.commons.exception.FunctionalException;
import com.asptt.resa.commons.exception.NotFound;
import com.asptt.resa.commons.exception.NotFoundException;
import com.asptt.resa.commons.service.ServiceBase;
import com.asptt.resabackend.entity.Adherent;
import com.asptt.resabackend.entity.ContactUrgent;
import com.asptt.resabackend.resources.contacturgent.ContactService;

@Service("adherentService")
public class AdherentServiceImpl extends ServiceBase<Adherent> implements AdherentService {

	@Autowired
	private AdherentDaoImpl adherentDao;

	@Autowired
	private ContactService contactService;

	@Override
	protected AdherentDaoImpl getDao() {
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
	 * @param String id : numero de license de l'adherent
	 * @return Adherent
	 */
	@Override
	public Adherent get(String id) {
		Adherent adh = super.get(id);
		if (null != adh) {
			return adh;
		} else {
			throw new NotFoundException(NotFound.GENERIC, "Numero de License [" + id + "] n'existe pas");
		}
	}

	@Override
	public Adherent create(Adherent resource) {
		try {
		this.get(resource.getNumeroLicense());
		} catch(NotFoundException e) {
			return super.create(resource);
		}
		throw new FunctionalException(Functional.REGISTRATION, "Creation Impossible, l'adherent avec le numero de license ["+resource.getNumeroLicense()+"] existe déjà");
	}

	@Override
	public List<ContactUrgent> findContacts(String adherentId) {
		return contactService.findContactsForAdherent(adherentId);
	}

	@Override
	public List<ContactUrgent> createContacts(String adherentId,List<ContactUrgent> resources) {
		Adherent adh = this.get(adherentId);
		return contactService.createContactsForAdherent(resources, adherentId);
	}

	@Override
	public void deleteContacts(String adherentId) {
		
		contactService.deleteContactsForAdherent(adherentId);
	}

	
}
