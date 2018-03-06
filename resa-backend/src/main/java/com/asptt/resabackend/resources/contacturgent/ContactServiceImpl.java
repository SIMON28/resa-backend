package com.asptt.resabackend.resources.contacturgent;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.asptt.resa.commons.exception.Functional;
import com.asptt.resa.commons.exception.FunctionalException;
import com.asptt.resa.commons.exception.NotFound;
import com.asptt.resa.commons.exception.NotFoundException;
import com.asptt.resa.commons.service.ServiceBase;
import com.asptt.resabackend.entity.ContactUrgent;

@Service("contactService")
public class ContactServiceImpl extends ServiceBase<ContactUrgent> implements ContactService {

	@Autowired
	private ContactDaoImp contactDao;

	@Override
	protected ContactDaoImp getDao() {
		return this.contactDao;
	}

	@Override
	public String getId(final ContactUrgent resource) {
		return resource.getId().toString();
	}

	@Override
	public void setId(ContactUrgent resource) {
		// TODO Auto-generated method stub

	}

	/**
	 * Description recherche un contact urgent avec son numero de telephone
	 * 
	 * @param String
	 *            tel : numero de telephone du contact
	 * @return ContactUrgent
	 */
	@Override
	public ContactUrgent get(String id) {
		ContactUrgent contact = super.get(id);
		if (null != contact) {
			return contact;
		} else {
			throw new NotFoundException(NotFound.GENERIC,
					"ContactUrgent avec cet id [" + id + "] n'existe pas");
		}
	}

	@Override
	public ContactUrgent create(ContactUrgent resource) {
		try {
			this.get(resource.getId().toString());
		} catch (NotFoundException e) {
			return super.create(resource);
		}
		throw new FunctionalException(Functional.REGISTRATION,
				"Creation Impossible, contactUrgent avec cet id ["+resource.getId()+"] existe déjà");
	}

	@Override
	public List<ContactUrgent> createContactsForAdherent(List<ContactUrgent> contacts, String adherentId) {
		// verification si le contact existe déja avec son numero de tel
		for (ContactUrgent contact : contacts) {
			if (null == contact.getTelephone() || contact.getTelephone().isEmpty()) {
				throw new FunctionalException(Functional.GENERIC,
						"Creation du contact impossible, numero de telephone d'un contact est vide ou null");
			}
		}
		return this.getDao().createContactsForAdherent(contacts, adherentId);
	}

	@Override
	public List<ContactUrgent> findContactsForAdherent(String adherentId) {
		return 	this.getDao().findContactsForAdherent(adherentId);
	}

	@Override
	public void deleteContactsForAdherent(String adherentId) {
		this.getDao().deleteContactsForAdherent(adherentId);
		
	}

}
