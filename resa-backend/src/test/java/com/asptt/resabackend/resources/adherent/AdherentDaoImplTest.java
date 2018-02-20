package com.asptt.resabackend.resources.adherent;

import java.util.Date;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.asptt.resa.commons.dao.Dao;
import com.asptt.resa.commons.exception.NotFoundException;
import com.asptt.resa.commons.exception.TechnicalException;
import com.asptt.resabackend.ApplicationTest;
import com.asptt.resabackend.entity.Adherent;
import com.asptt.resabackend.entity.NiveauAutonomie;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration (classes=ApplicationTest.class)
@ActiveProfiles(profiles = "test")
public class AdherentDaoImplTest {
	
	private static final org.slf4j.Logger LOGGER = LoggerFactory
			.getLogger(AdherentDaoImplTest.class);


	@Autowired
	private Dao<Adherent> adherentDao;
		
	@Test
	public void getAdherentById() {
		Adherent adh = adherentDao.get("096042");
		Assert.assertEquals(adh.getNumeroLicense(), "096042");
	}

	@Test
	public void getAdherentByUnknownId() {
		try {
		Adherent adh = adherentDao.get("9lpm25");
		} catch(NotFoundException e) {
			Assert.assertEquals("404-0", e.getCategory()+"-"+e.getCode().getCode());
		}
	}

	@Test
	public void testUpdate() {
		Adherent adh = new Adherent();
		adh.setNumeroLicense("999998");
		adh.setNiveau(NiveauAutonomie.BATM.name());
		adh.setNom("ACCUEIL");
		adh.setPrenom("toto");
		adh.setEnumNiveau(NiveauAutonomie.P0);
		adh.setTelephone("0491163590");
		adh.setMail("titi.toto@orange.fr");
		adh.setEncadrement(null);
		adh.setPilote(false);
		adh.setActif(false);
		adh.setTiv(false);
		adh.setDateCM(new Date());
		adh.setAnneeCotisation(2000);
		adh.setCommentaire("");
		try {
			Adherent adhUpdated = adherentDao.update(adh);
			Assert.assertEquals("toto", adhUpdated.getPrenom());
			LOGGER.info("On a bien une mise à jour");
		} catch (TechnicalException e) {
			LOGGER.debug("mise à jour d'un adherent plantée"+e.getMessage());
			Assert.fail("mise à jour d'un adherent plantée");
			// La duplication a bien été controlée
		}
	}
}
