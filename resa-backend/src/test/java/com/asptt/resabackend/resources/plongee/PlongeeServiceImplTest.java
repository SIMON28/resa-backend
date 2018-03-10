package com.asptt.resabackend.resources.plongee;

import java.util.ArrayList;

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
import com.asptt.resabackend.ApplicationTest;
import com.asptt.resabackend.entity.Plongee;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = ApplicationTest.class)
@ActiveProfiles(profiles = "test")
public class PlongeeServiceImplTest {

	private static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(PlongeeServiceImplTest.class);

	@Autowired
	private PlongeeServiceImpl plongeeService;

	@Test
	public void getPlongeeById() {
		Plongee plongee = plongeeService.get("2634");
		LOGGER.info("ok pour getPlongeeById");
		Assert.assertEquals(plongee.getId(), new Integer("2634"));
	}

	@Test
	public void getPlongeeByUnknownId() {
		try {
			plongeeService.get("99999999");
		} catch (NotFoundException e) {
			LOGGER.info("Plongee non trouve");
			Assert.assertEquals("404-0", e.getCategory() + "-" + e.getCode().getCode());
		}
	}

	@Test
	public void findPlongees() {
		ArrayList<Plongee> plongees = (ArrayList<Plongee>) plongeeService.find();
		LOGGER.info("ok pour findPlongees");
		Assert.assertEquals(plongees.size(), 10);
	}

	//	@Test
	public void create() {
		Plongee adh = new Plongee();
//		adh.setNumeroLicense("unnumero");
//		adh.setNiveau(NiveauAutonomie.BATM.name());
//		adh.setNom("TESTCREATE");
//		adh.setPrenom("toto");
//		adh.setEnumNiveau(NiveauAutonomie.P0);
//		adh.setTelephone("0499999999");
//		adh.setMail("test.test@orange.fr");
//		adh.setEncadrement(null);
//		adh.setPilote(false);
//		adh.setActif(false);
//		adh.setTiv(false);
//		adh.setDateCM(new Date());
//		adh.setAnneeCotisation(2000);
//		adh.setCommentaire("");
//		List<String> l_roles = new ArrayList<>();
//		l_roles.add(Roles.ADMIN.name());
//		l_roles.add(Roles.USER.name());
//		l_roles.add(Roles.SECRETARIAT.name());
//		adh.setRoles(l_roles);

		try {
			Plongee newUpdated = plongeeService.create(adh);
			Assert.assertEquals("TESTCREATE", newUpdated.getEnumNiveauMinimum());
			Assert.assertEquals(adh, newUpdated);
			LOGGER.info("On a bien une creation");
		} catch (TechnicalException e) {
			LOGGER.debug("creation d'un adherent plantée" + e.getMessage());
		} catch (FunctionalException f) {
			Assert.assertEquals("Creation Impossible, l'adherent avec le numero de license [unnumero] existe déjà",
					f.getMessage());
		}
	}

//	@Test
	public void update() {
		Plongee adh = new Plongee();
//		adh.setNumeroLicense("999998");
//		adh.setNiveau(NiveauAutonomie.BATM.name());
//		adh.setNom("ACCUEIL");
//		adh.setPrenom("toto");
//		adh.setEnumNiveau(NiveauAutonomie.P0);
//		adh.setTelephone("0491163590");
//		adh.setMail("titi.toto@orange.fr");
//		adh.setEncadrement(null);
//		adh.setPilote(false);
//		adh.setActif(false);
//		adh.setTiv(false);
//		adh.setDateCM(new Date());
//		adh.setAnneeCotisation(2000);
//		adh.setCommentaire("");
//		List<String> l_roles = new ArrayList<>();
//		l_roles.add(Roles.ADMIN.name());
//		l_roles.add(Roles.USER.name());
//		l_roles.add(Roles.SECRETARIAT.name());
//		adh.setRoles(l_roles);
//		List<String> contacts = new ArrayList<>();
//		contacts.add("113");
//		adh.setContacts(contacts);

		try {
			Plongee adhUpdated = plongeeService.update(adh);
			Assert.assertEquals("toto", adhUpdated.getEnumNiveauMinimum());
			Assert.assertEquals(adh, adhUpdated);
			LOGGER.info("On a bien une mise à jour");
		} catch (TechnicalException e) {
			LOGGER.debug("mise à jour d'un adherent plantée" + e.getMessage());
		}
	}


}
