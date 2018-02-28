package com.asptt.resabackend.resources.adherent;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.asptt.resa.commons.dao.Dao;
import com.asptt.resa.commons.exception.NotFound;
import com.asptt.resa.commons.exception.NotFoundException;
import com.asptt.resa.commons.service.ServiceBase;
import com.asptt.resabackend.entity.Adherent;

@Service("adherentService")
public class AdherentServiceImpl extends ServiceBase<Adherent> implements AdherentService {

	@Autowired
	private Dao<Adherent> adherentDao;

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

	@Override
	public Adherent get(String id) {
		Adherent adh = super.get(id);
		if (null != adh) {
			return adh;
		} else {
			throw new NotFoundException(NotFound.GENERIC, "Numero de License [" + id + "] n'existe pas");
		}
	}

}
