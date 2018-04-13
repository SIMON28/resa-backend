package com.asptt.resabackend.resources.plongee;

import java.util.List;

import javax.ws.rs.core.MultivaluedMap;

import com.asptt.resa.commons.dao.Dao;
import com.asptt.resabackend.entity.Plongee;
import com.asptt.resabackend.entity.TypeActionReturnPlongee;

public interface PlongeeDao extends Dao<Plongee> {

	Integer findCount(MultivaluedMap<String, String> criteria);

	List<Plongee> findPlongeeForEncadrant(String nbJourReserv, String nbHourApres);

	List<Plongee> findPlongeeForAdherent(TypeActionReturnPlongee action);

}
