package com.asptt.resabackend.resources.adherent;

import java.util.List;

import com.asptt.resa.commons.service.Service;
import com.asptt.resabackend.entity.Adherent;
import com.asptt.resabackend.entity.ContactUrgent;

public interface AdherentService extends Service<Adherent> {

	List<ContactUrgent> findContacts(String adherentId);

	List<ContactUrgent> createContacts(String adherentId, List<ContactUrgent> resources);

	void deleteContacts(String adherentId);

}
