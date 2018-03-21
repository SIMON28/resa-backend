package com.asptt.resabackend.resources.adherent;

import javax.ws.rs.core.MultivaluedMap;

import com.asptt.resa.commons.dao.Dao;
import com.asptt.resabackend.entity.Adherent;
import com.asptt.resabackend.entity.Adherent.Roles;

public interface AdherentDao extends Dao<Adherent> {

	void createRoleForAdherent(String id, Roles role);

	void deleteRoleForAdherent(String id, Roles role);

	void createContactForAdherent(String id, String contactId);

	void deleteContactForAdherent(String id, String contactId);

	Integer findCount(MultivaluedMap<String, String> criteria);

}
