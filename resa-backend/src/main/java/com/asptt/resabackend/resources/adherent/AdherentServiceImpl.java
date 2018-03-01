package com.asptt.resabackend.resources.adherent;


import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.asptt.resa.commons.dao.Dao;
import com.asptt.resa.commons.exception.Functional;
import com.asptt.resa.commons.exception.FunctionalException;
import com.asptt.resa.commons.exception.NotFound;
import com.asptt.resa.commons.exception.NotFoundException;
import com.asptt.resa.commons.service.ServiceBase;
import com.asptt.resabackend.entity.Adherent;
import com.asptt.resabackend.entity.ContactUrgent;

@Service("adherentService")
public class AdherentServiceImpl extends ServiceBase<Adherent, ContactUrgent> implements AdherentService {

	@Autowired
	private Dao<Adherent> adherentDao;

	@Autowired
	private Dao<ContactUrgent> contactUrgentDao;

	@Override
	protected Dao<Adherent> getDao() {
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
	public List<ContactUrgent> findSousResource(String resouceId) {
		Adherent adh = this.getDao().get(resouceId);
		List<String> contacts = adh.getContacts();
		List<ContactUrgent> contactUrgents = new ArrayList<>();
		for (String contactId : contacts) {
			contactUrgents.add(contactUrgentDao.get(contactId));
		}
		return contactUrgents;
	}

}
