package com.asptt.resabackend.resources.adherent;

import static org.mockito.Mockito.when;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.ws.rs.core.MultivaluedHashMap;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.UriInfo;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.asptt.resa.commons.exception.FunctionalException;
import com.asptt.resa.commons.exception.NotFoundException;
import com.asptt.resa.commons.exception.TechnicalException;
import com.asptt.resabackend.Application;
import com.asptt.resabackend.entity.Adherent;
import com.asptt.resabackend.entity.ContactUrgent;
import com.asptt.resabackend.entity.NiveauAutonomie;
import com.asptt.resabackend.entity.Plongee;
import com.asptt.resabackend.entity.TypeRoles;
import com.asptt.resabackend.util.ResaBackendMessage;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration (classes=Application.class)
//@ActiveProfiles(profiles = "test")
public class AdherentServiceImplTest {
	
	private static final org.slf4j.Logger LOGGER = LoggerFactory
			.getLogger(AdherentServiceImplTest.class);


	@Autowired
	private AdherentService adherentService;
		
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
			Assert.assertEquals(MessageFormat.format(ResaBackendMessage.ADHERENT_NOT_FOUND,"9lpm25"), e.getMessage());
		}
	}

	@Test
	public void create() {
		Adherent adh = new Adherent();
		adh.setNumeroLicense("unnumero");
		adh.setNiveau(NiveauAutonomie.BATM);
		adh.setNom("TESTCREATE");
		adh.setPrenom("toto");
		adh.setNiveau(NiveauAutonomie.P0);
		adh.setTelephone("0499999999");
		adh.setMail("test.test@orange.fr");
		adh.setEncadrement(null);
		adh.setPilote(false);
		adh.setActif(1);
		adh.setTiv(false);
		adh.setDateCM(new Date());
		adh.setAnneeCotisation(2000);
		adh.setCommentaire("");
		List<String> l_roles = new ArrayList<>();
		l_roles.add(TypeRoles.ADMIN.name());
		l_roles.add(TypeRoles.USER.name());
		l_roles.add(TypeRoles.SECRETARIAT.name());
		adh.setRoles(l_roles);
		
		try {
			Adherent newUpdated = adherentService.create(adh);
			Assert.assertEquals("TESTCREATE", newUpdated.getNom());
			Assert.assertEquals(adh, newUpdated);
			LOGGER.info("On a bien une creation");
		} catch (TechnicalException e) {
			LOGGER.debug("creation d'un adherent plantée"+e.getMessage());
		} catch (FunctionalException f) {
			LOGGER.debug("Creation Impossible,"+f.getMessage());
			Assert.assertEquals("Creation Impossible, l'adherent avec le numero de license [unnumero] existe déjà", f.getMessage());
		}
	}

	@Test
	public void update() {
		Adherent adh = new Adherent();
//		adh.setNumeroLicense("");
		adh.setNiveau(NiveauAutonomie.BATM);
		adh.setNom("TESTCREATE");
		adh.setPrenom("toto");
		adh.setNiveau(NiveauAutonomie.P0);
		adh.setTelephone("0123456789");
		adh.setMail("titi.toto@orange.fr");
		adh.setEncadrement(null);
		adh.setPilote(false);
		adh.setActif(1);
		adh.setTiv(false);
		adh.setDateCM(new Date());
		adh.setAnneeCotisation(2000);
		adh.setCommentaire("");
		List<String> l_roles = new ArrayList<>();
		l_roles.add(TypeRoles.ADMIN.name());
		l_roles.add(TypeRoles.USER.name());
		l_roles.add(TypeRoles.SECRETARIAT.name());
		adh.setRoles(l_roles);
		List<String> contacts = new ArrayList<>();
//		contacts.add("113");
		adh.setContacts(contacts);
		
		try {
			Adherent adhUpdated = adherentService.update("unnumero",adh);
			Assert.assertEquals("0123456789", adhUpdated.getTelephone());
			Assert.assertEquals(adh, adhUpdated);
			LOGGER.info("On a bien une mise à jour");
		} catch (TechnicalException e) {
			LOGGER.debug("mise à jour d'un adherent plantée"+e.getMessage());
		}
	}
	
//	@Test
	public void findPlongees() {
		UriInfo uriInfo = Mockito.mock(UriInfo.class);
		MultivaluedMap<String, String> queryParameters = new MultivaluedHashMap<>();
		queryParameters.add("action", "consulter");
		queryParameters.add("param2", "value2");
		queryParameters.add("fields", "toto,titi,tata");
		
		when(uriInfo.getQueryParameters()).thenReturn(queryParameters);
		List<Plongee> plongees = adherentService.findPlongees(uriInfo, "096042");
		Assert.assertEquals(4,plongees.size());
	}
	
	@Test
	public void getContactUrgent() {
		List<ContactUrgent> contact = adherentService.findContacts("096042");
		Assert.assertEquals("DEFURNE", contact.get(0).getNom());
	}

	@Test
	public void createContactUrgent() {
		List<ContactUrgent> contacts = adherentService.findContacts("096042");
		List<ContactUrgent> contactCreated = adherentService.createContacts("unnumero", contacts);
		Assert.assertEquals(contacts, contactCreated);
	}

	@Test
	public void deleteContactUrgent() {
		adherentService.deleteContacts("unnumero");
	}

}
