package com.asptt.resabackend.resources.plongee;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.asptt.resa.commons.dao.Dao;
import com.asptt.resa.commons.exception.NotFound;
import com.asptt.resa.commons.exception.NotFoundException;
import com.asptt.resa.commons.service.ServiceBase;
import com.asptt.resabackend.entity.Adherent;
//import com.asptt.resabackend.commons.service.ServiceBaseResa;
import com.asptt.resabackend.entity.Plongee;
import com.asptt.resabackend.resources.adherent.AdherentServiceImpl;

@Service("plongeeService")
public class PlongeeServiceImpl extends ServiceBase<Plongee, Object> implements PlongeeService {

	@Autowired
	private Dao<Plongee> plongeeDao;

	@Autowired
	AdherentServiceImpl adherentService;

	@Override
	protected Dao<Plongee> getDao() {
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

	@Override
	public Plongee get(String id) {
		Plongee plongee = super.get(id);
		if (null != plongee) {
			for (String adhId : plongee.getParticipants()) {
				Adherent adh = adherentService.get(adhId);
				if (adh.isPilote()) {
					plongee.setPilote(adhId);
				}
				if (adh.isDp()) {
					plongee.setDp(adhId);
				}
			}
		} else {
			throw new NotFoundException(NotFound.GENERIC, "id plongee [" + id + "] n'existe pas");
		}
		return plongee;
	}

	@Override
	public List<Object> findSousResource(String id) {
		// TODO Auto-generated method stub
		return null;
	}

}
