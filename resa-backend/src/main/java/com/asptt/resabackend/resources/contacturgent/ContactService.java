package com.asptt.resabackend.resources.contacturgent;


import java.util.List;

import com.asptt.resa.commons.service.Service;
import com.asptt.resabackend.entity.ContactUrgent;

public interface ContactService extends Service<ContactUrgent> {

	List<ContactUrgent> createContactsForAdherent(List<ContactUrgent> resources, String adherentId);

	List<ContactUrgent> findContactsForAdherent(String contactId);

	void deleteContactsForAdherent(String adherentId);

}
