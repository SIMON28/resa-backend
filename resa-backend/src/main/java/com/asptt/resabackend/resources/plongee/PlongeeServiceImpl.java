package com.asptt.resabackend.resources.plongee;

import java.util.List;
import java.util.Map;

import javax.ws.rs.core.MultivaluedMap;

import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.asptt.resa.commons.exception.NotFound;
import com.asptt.resa.commons.exception.NotFoundException;
import com.asptt.resa.commons.service.ServiceBase;
import com.asptt.resabackend.entity.Adherent;
//import com.asptt.resabackend.commons.service.ServiceBaseResa;
import com.asptt.resabackend.entity.Plongee;
import com.asptt.resabackend.resources.adherent.AdherentServiceImpl;
import com.asptt.resabackend.util.Parameters;

@Service("plongeeService")
public class PlongeeServiceImpl extends ServiceBase<Plongee> implements PlongeeService {

	private static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(PlongeeServiceImpl.class);

	@Autowired
	private PlongeeDaoImpl plongeeDao;

	@Autowired
	AdherentServiceImpl adherentService;

	@Override
	protected PlongeeDaoImpl getDao() {
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
	public List<Plongee> find(MultivaluedMap<String, String> criteria) {
		Parameters.getInt("plongee.limit");
		criteria.containsKey("limit");
		for (Map.Entry<String, List<String>> entry : criteria.entrySet()) {
			LOGGER.info("clé=["+entry.getKey()+"] valeur=["+entry.getValue().get(0)+"]");
		}

		return super.find(criteria);
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


}
