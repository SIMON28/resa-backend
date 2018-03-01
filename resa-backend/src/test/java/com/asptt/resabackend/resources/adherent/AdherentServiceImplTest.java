package com.asptt.resabackend.resources.adherent;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.asptt.resa.commons.exception.FunctionalException;
import com.asptt.resa.commons.exception.NotFoundException;
import com.asptt.resa.commons.exception.TechnicalException;
import com.asptt.resa.commons.service.Service;
import com.asptt.resabackend.ApplicationTest;
import com.asptt.resabackend.entity.Adherent;
import com.asptt.resabackend.entity.Adherent.Roles;
import com.asptt.resabackend.entity.ContactUrgent;
import com.asptt.resabackend.entity.NiveauAutonomie;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration (classes=ApplicationTest.class)
@ActiveProfiles(profiles = "test")
public class AdherentServiceImplTest {
	
	private static final org.slf4j.Logger LOGGER = LoggerFactory
			.getLogger(AdherentServiceImplTest.class);


	@Autowired
	private Service<Adherent, ContactUrgent> adherentService;
		
	@Test
	public void getAdherentById() {
		Adherent adh = adherentService.get("096042");
		LOGGER.info("ok pour getAdherentById");
		Assert.assertEquals(adh.getNumeroLicense(), "096042");
	}

	@Test
	public void getAdherentByUnknownId() {
		try {
		adherentService.get("9lpm25");
		} catch(NotFoundException e) {
			LOGGER.info("Adherent non trouve");
			Assert.assertEquals("404-0", e.getCategory()+"-"+e.getCode().getCode());
		}
	}

	@Test
	public void testCreate() {
		Adherent adh = new Adherent();
		adh.setNumeroLicense("unnumero");
		adh.setNiveau(NiveauAutonomie.BATM.name());
		adh.setNom("TESTCREATE");
		adh.setPrenom("toto");
		adh.setEnumNiveau(NiveauAutonomie.P0);
		adh.setTelephone("0499999999");
		adh.setMail("test.test@orange.fr");
		adh.setEncadrement(null);
		adh.setPilote(false);
		adh.setActif(false);
		adh.setTiv(false);
		adh.setDateCM(new Date());
		adh.setAnneeCotisation(2000);
		adh.setCommentaire("");
		List<String> l_roles = new ArrayList<>();
		l_roles.add(Roles.ADMIN.name());
		l_roles.add(Roles.USER.name());
		l_roles.add(Roles.SECRETARIAT.name());
		adh.setRoles(l_roles);
		
		try {
			Adherent newUpdated = adherentService.create(adh);
			Assert.assertEquals("TESTCREATE", newUpdated.getNom());
			Assert.assertEquals(adh, newUpdated);
			LOGGER.info("On a bien une creation");
		} catch (TechnicalException e) {
			LOGGER.debug("creation d'un adherent plantée"+e.getMessage());
		} catch (FunctionalException f) {
			Assert.assertEquals("Creation Impossible, l'adherent avec le numero de license [unnumero] existe déjà", f.getMessage());
		}
	}

//	@Test
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
		List<String> l_roles = new ArrayList<>();
		l_roles.add(Roles.ADMIN.name());
		l_roles.add(Roles.USER.name());
		l_roles.add(Roles.SECRETARIAT.name());
		adh.setRoles(l_roles);
		List<String> contacts = new ArrayList<>();
		contacts.add("113");
		adh.setContacts(contacts);
		
		try {
			Adherent adhUpdated = adherentService.update(adh);
			Assert.assertEquals("toto", adhUpdated.getPrenom());
			Assert.assertEquals(adh, adhUpdated);
			LOGGER.info("On a bien une mise à jour");
		} catch (TechnicalException e) {
			LOGGER.debug("mise à jour d'un adherent plantée"+e.getMessage());
		}
	}
}
