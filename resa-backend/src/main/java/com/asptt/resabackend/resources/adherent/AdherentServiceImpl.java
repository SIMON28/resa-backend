package com.asptt.resabackend.resources.adherent;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.asptt.resa.commons.dao.Dao;
import com.asptt.resabackend.commons.service.ServiceBaseResa;
import com.asptt.resabackend.entity.Adherent;


@Service("adherentService")
public class AdherentServiceImpl extends ServiceBaseResa<Adherent> implements AdherentService {

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

}
