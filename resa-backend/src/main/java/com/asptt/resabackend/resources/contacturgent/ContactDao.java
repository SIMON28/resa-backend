package com.asptt.resabackend.resources.contacturgent;


import java.util.List;

import com.asptt.resa.commons.dao.Dao;
import com.asptt.resabackend.entity.ContactUrgent;

public interface ContactDao extends Dao<ContactUrgent> {

	List<ContactUrgent> createContactsForAdherent(List<ContactUrgent> contacts, String adherentId);

	List<ContactUrgent> findContactsForAdherent(String adherentId);

	void deleteContactsForAdherent(String adherentId);

}
