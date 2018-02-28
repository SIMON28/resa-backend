package com.asptt.resabackend.resources.plongee;

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
import com.asptt.resabackend.ApplicationTest;
import com.asptt.resabackend.entity.Plongee;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration (classes=ApplicationTest.class)
@ActiveProfiles(profiles = "test")
public class PlongeeDaoImplTest {
	
	private static final org.slf4j.Logger LOGGER = LoggerFactory
			.getLogger(PlongeeDaoImplTest.class);


	@Autowired
	private Dao<Plongee> plongeeDao;
		
	@Test
	public void getPlongeeById() {
		Plongee plongee = plongeeDao.get("2634");
		LOGGER.info("ok pour getPlongeeById");
		Assert.assertEquals(Integer.toString(plongee.getId()), "2634");
	}

	@Test
	public void getPlongeeByUnknownId() {
		try {
		Plongee adh = plongeeDao.get("9lpm25");
		} catch(NotFoundException e) {
			Assert.assertEquals("404-0", e.getCategory()+"-"+e.getCode().getCode());
		}
	}

//	@Test
//	public void testUpdate() {
//		Plongee plongee = new Plongee();
//		plongee.setNumeroLicense("999998");
//		plongee.setNiveau(NiveauAutonomie.BATM.name());
//		plongee.setNom("ACCUEIL");
//		plongee.setPrenom("toto");
//		plongee.setEnumNiveau(NiveauAutonomie.P0);
//		plongee.setTelephone("0491163590");
//		plongee.setMail("titi.toto@orange.fr");
//		plongee.setEncadrement(null);
//		plongee.setPilote(false);
//		plongee.setActif(false);
//		plongee.setTiv(false);
//		plongee.setDateCM(new Date());
//		plongee.setAnneeCotisation(2000);
//		plongee.setCommentaire("");
//		List<String> l_roles = new ArrayList<>();
//		l_roles.add(Roles.ADMIN.name());
//		l_roles.add(Roles.USER.name());
//		l_roles.add(Roles.SECRETARIAT.name());
//		adh.setRoles(l_roles);
//		try {
//			Plongee plongeeUpdated = plongeeDao.update(plongee);
//			Assert.assertEquals("toto", plongeeUpdated.getDateReservation());
//			LOGGER.info("On a bien une mise à jour");
//		} catch (TechnicalException e) {
//			LOGGER.debug("mise à jour d'un plongee plantée"+e.getMessage());
//			Assert.fail("mise à jour d'un plongee plantée");
//			// La duplication a bien été controlée
//		}
//	}
}
