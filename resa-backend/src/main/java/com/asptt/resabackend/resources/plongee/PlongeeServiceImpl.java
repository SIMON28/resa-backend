package com.asptt.resabackend.resources.plongee;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.ws.rs.core.MultivaluedMap;

import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.asptt.resa.commons.service.ServiceBase;
import com.asptt.resabackend.entity.Adherent;
//import com.asptt.resabackend.commons.service.ServiceBaseResa;
import com.asptt.resabackend.entity.Plongee;
import com.asptt.resabackend.entity.TypeActionReturnPlongee;
import com.asptt.resabackend.resources.adherent.AdherentService;

@Service("plongeeService")
public class PlongeeServiceImpl extends ServiceBase<Plongee> implements PlongeeService {

	private static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(PlongeeServiceImpl.class);

	@Autowired
	private PlongeeDao plongeeDao;

	@Autowired
	AdherentService adherentService;

	@Override
	protected PlongeeDao getDao() {
		return this.plongeeDao;
	}

	@Override
	public String getId(final Plongee resource) {
		return Integer.toString(resource.getId());
	}

	@Override
	public void setId(Plongee resource) {
		// TODO Auto-generated method stub

	}

	public Integer findCount(MultivaluedMap<String, String> criteria) {
		return getDao().findCount(criteria);
	}

	@Override
	public List<Plongee> find() {
		return setDpPilote(super.find());
	}

	@Override
	public List<Plongee> find(MultivaluedMap<String, String> criteria) {
		for (Map.Entry<String, List<String>> entry : criteria.entrySet()) {
			LOGGER.info("clé=[" + entry.getKey() + "] valeur=[" + entry.getValue().get(0) + "]");
		}

		return setDpPilote(super.find(criteria));
	}

	@Override
	public Plongee get(String id) {
		Plongee plongee = super.get(id);
		List<Plongee> plongees = new ArrayList<>();
		plongees.add(plongee);
		setDpPilote(plongees);
		return plongees.get(0);
	}

	private List<Plongee> setDpPilote(List<Plongee> plongees) {
		List<Plongee> updatedPlongees = new ArrayList<>();
		for (Plongee plongee : plongees) {
			for (String adhId : plongee.getParticipants()) {
				Adherent adh = adherentService.get(adhId);
				if (adh.isPilote()) {
					plongee.setPilote(adhId);
				}
				if (adh.isDp()) {
					plongee.setDp(adhId);
				}
			}
			updatedPlongees.add(plongee);
		}
		return updatedPlongees;
	}

	@Override
	public List<Plongee> findPlongeeForEncadrant(String nbJourReserv, String nbHourApres) {
		List<Plongee> plongees = getDao().findPlongeeForEncadrant(nbJourReserv, nbHourApres);
		return setDpPilote(plongees);
	}

	@Override
	public List<Plongee> findPlongeeForAdherent(TypeActionReturnPlongee action) {
		List<Plongee> plongees = getDao().findPlongeeForAdherent(action);
		return setDpPilote(plongees);
	}

	@Override
	public List<String> getAdherentsInscrits(Integer plongeeId, Object object, Object object2, Object object3) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<String> getAdherentsWaiting(Integer plongeeId) {
		// TODO Auto-generated method stub
		return null;
	}

}
